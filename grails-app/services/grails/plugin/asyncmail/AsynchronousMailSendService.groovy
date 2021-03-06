package grails.plugin.asyncmail

import grails.plugins.mail.MailService
import groovy.transform.CompileStatic
import org.springframework.mail.MailMessage

@CompileStatic
class AsynchronousMailSendService {
    MailService mailService

    MailMessage send(AsynchronousMailMessage message) {
        return mailService.sendMail {
            if (isMimeCapable() && (message.attachments || (message.html && message.alternative))) {
                multipart true
            }
            if(message.to && !message.to.isEmpty()){
                to message.to
            }
            subject message.subject
            if (message.headers && !message.headers.isEmpty() && isMimeCapable()) {
                headers message.headers
            }
            if (message.html && isMimeCapable()) {
                html message.text
                if(message.alternative){
                    text message.alternative
                }
            } else {
                body message.text
            }
            if (message.bcc && !message.bcc.isEmpty()) {
                bcc message.bcc
            }
            if (message.cc && !message.cc.isEmpty()) {
                cc message.cc
            }
            if (message.replyTo) {
                replyTo message.replyTo
            }
            if (message.from) {
                from message.from
            }
            if(message.envelopeFrom){
                envelopeFrom message.envelopeFrom
            }
            if (isMimeCapable()) {
                message.attachments?.each {AsynchronousMailAttachment attachment ->
                    if (!attachment.inline) {
                        attachBytes attachment.attachmentName, attachment.mimeType, attachment.content
                    } else {
                        inline attachment.attachmentName, attachment.mimeType, attachment.content
                    }
                }
            }
        }
    }
}
