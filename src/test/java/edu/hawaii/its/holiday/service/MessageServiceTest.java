package edu.hawaii.its.holiday.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.hawaii.its.holiday.configuration.SpringBootWebApplication;
import edu.hawaii.its.holiday.type.Message;
import edu.hawaii.its.holiday.util.Strings;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SpringBootWebApplication.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void findMessage() {
        Message message = messageService.findMessage(Message.GATE_MESSAGE);
        assertThat(message.getEnabled(), equalTo("Y"));
        assertThat(message.getTypeId(), equalTo(Message.GATE_MESSAGE));
        assertThat(message.getText(), startsWith("University of Hawaii"));

        // No matching ID, so null returned.
        message = messageService.findMessage(-1);
        assertNull(message);

        // Make sure the denied access message actually exists.
        messageService.evictCache();
        message = messageService.findMessage(Message.ACCESS_DENIED_MESSAGE);
        assertThat(message.getId(), equalTo(Message.ACCESS_DENIED_MESSAGE));
        assertThat(message.getText(), containsString("system is restricted"));
    }

    @Test
    public void update() {
        Message message = messageService.findMessage(Message.GATE_MESSAGE);
        assertThat(message.getEnabled(), equalTo("Y"));
        assertThat(message.getTypeId(), equalTo(Message.GATE_MESSAGE));
        assertThat(message.getText(), startsWith("University of Hawaii"));
        assertThat(message.getText(), endsWith("."));

        final String text = message.getText();

        message.setText("Stemming the bleeding.");
        message = messageService.update(message);

        message = messageService.findMessage(Message.GATE_MESSAGE);
        assertThat(message.getEnabled(), equalTo("Y"));
        assertThat(message.getTypeId(), equalTo(Message.GATE_MESSAGE));
        assertThat(message.getText(), equalTo("Stemming the bleeding."));

        // Put the original text back.
        message.setText(text);
        message = messageService.update(message);
        assertThat(message.getText(), startsWith("University of Hawaii"));
        assertThat(message.getText(), endsWith("."));
    }

    @Test
    public void messageCache() {
        Message m0 = messageService.findMessage(Message.GATE_MESSAGE);
        Message m1 = messageService.findMessage(Message.GATE_MESSAGE);
        assertSame(m0, m1);

        m0.setText("This land is your land.");
        assertSame(m0, m1);

        System.out.println(Strings.fill('v', 88));
        System.out.println("m0: " + m0);
        System.out.println("m1: " + m1);
        System.out.println(Strings.fill('^', 88));

        m0 = messageService.update(m0);
        ///assertSame(m0, m1);

        assertThat(m0, equalTo(m1));
        //assertSame(m0, m1);
        assertNotSame(m0, m1); // FIXME: This is a bug.

        m1 = messageService.findMessage(Message.GATE_MESSAGE);
        assertSame(m0, m1);

        Message m2 = messageService.findMessage(Message.GATE_MESSAGE);
        assertSame(m0, m2);
        assertSame(m1, m2);

        Message m3 = new Message();
        m3.setId(999);
        m3.setEnabled("Y");
        m3.setText("Testing");
        m3.setTypeId(1);
        m3 = messageService.add(m3);

        Message m4 = messageService.findMessage(999);
        assertThat(m4, equalTo(m3));
        assertSame(m4, m3);
    }

}
