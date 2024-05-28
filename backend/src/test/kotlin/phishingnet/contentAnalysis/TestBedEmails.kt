package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.Sender


const val testAuthStr = """
        Authentication-Results: spf=pass (sender IP is 209.85.222.48)
        smtp.mailfrom=valimail.com; dkim=pass (signature was verified)
        header.d=valimail.com;dmarc=pass action=none
        header.from=valimail.com;compauth=pass reason=100
    """


const val realPhishingBody1 =
    "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><p><img src=\"images/stansted-airport-college/Stansted-Airport-College-Logo-400px.png\" alt=\"\"></p><h3>Thank you for contacting Stansted Airport College. We'll get back to you as soon as possible to answer your question.</h3><p><strong>Course you are interested in:</strong> Movies, videos</p><p><strong>Your question was:</strong> Hi, this is Irina. I am sending you my intimate photos as I promised. https://tinyurl.com/226pfc9j#VvxRlR</p><p>If you have any other questions please call one of our friendly Admissions Advisers on <strong>01279 868100</strong></p><p>&nbsp;</p></body></html>"
const val realPhishingHeaders1 = """
        Authentication-Results: spf=fail (sender IP is 109.169.81.118) 
        smtp.mailfrom=harlow-college.ac.uk; dkim=none (message not signed) 
        header.d=none;dmarc=temperror action=none header.from=harlow-college.ac.uk;
    """
const val realPhishingCleanContent1 = """
        Thank you for contacting Stansted Airport College. We'll get back to you as soon as possible to answer your question.
        Course you are interested in: Movies, videos
        Your question was: Hi, this is Irina. I am sending you my intimate photos as I promised. https://tinyurl.com/226pfc9j#VvxRlR
        If you have any other questions please call one of our friendly Admissions Advisers on 01279 868100
    """


const val rawBody1 =
    """<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div dir=\"ltr\">Boa tarde<div><br><div>Este email serve como teste</div></div><div><br></div><div>Cumprimentos</div><div>sender</div></div></body></html>"""
const val expectedCleanedContent1 = "Boa tarde Este email serve como teste Cumprimentos sender"

val testEmailEmpty = Email(
    Sender("1", "email1@test.com"),
    Sender("1", "email1@test.com"),
    "test",
    "Medium",
    hasAttachments = false,
    isRead = false,
    Sender("1", "email1@test.com"),
    testAuthStr,
    ""
)

val testEmail = Email(
    Sender("1", "email1@test.com"),
    Sender("1", "email1@test.com"),
    "test",
    "Medium",
    hasAttachments = false,
    isRead = false,
    Sender("1", "email1@test.com"),
    testAuthStr,
    rawBody1
)

val realPhishingEmail1 = Email(
    Sender("Stansted Airport College Enquiry", "admissions@harlow-college.ac.uk"),
    Sender("Stansted Airport College Enquiry", "admissions@harlow-college.ac.uk"),
    "Thanks for getting in touch\n",
    "",
    hasAttachments = false,
    isRead = false,
    Sender("Stansted Airport College Enquiry", "admissions@harlow-college.ac.uk"),
    realPhishingHeaders1,
    realPhishingBody1
)

