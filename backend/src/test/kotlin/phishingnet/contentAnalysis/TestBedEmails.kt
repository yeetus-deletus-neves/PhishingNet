package phishingnet.contentAnalysis

import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.Sender


const val testAuthStr = """
        Authentication-Results: spf=pass (sender IP is 209.85.222.48)
        smtp.mailfrom=valimail.com; dkim=pass (signature was verified)
        header.d=valimail.com;dmarc=pass action=none
        header.from=valimail.com;compauth=pass reason=100
    """

const val testFailingAuthStr = """
        Authentication-Results: spf=fail (sender IP is 209.85.222.48)
        smtp.mailfrom=valimail.com; dkim=none (signature was verified)
        header.d=valimail.com;dmarc=fail action=none
        header.from=valimail.com;compauth=pass reason=100
    """

const val realPromotionalEmailHeaders = """
        Authentication-Results: spf=pass (sender IP is 198.2.185.113) smtp.mailfrom=mail113.suw111.mcdlv.net; 
        dkim=pass (signature was verified) header.d=bicyclecards.com;
        dmarc=pass action=none header.from=bicyclecards.com;compauth=pass reason=100
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

const val realPromotionalEmailBody = """
    <html><head>\r\n<meta http-equiv=\"Content-Type\" 
    content=\"text/html; charset=utf-8\"><meta 
    content=\"IE=edge\"><meta name=\"viewport\" 
    content=\"width=device-width, 
    initial-scale=1\"><style>\r\n<!--\r\nimg\r\n\t{}\r\ntable, td\r\n\t{}\r\n.mceStandardButton, .mceStandardButton td, .mceStandardButton td a\r\n\t{}\r\np, a, li, td, blockquote\r\n\t{}\r\np, a, li, td, body, table, blockquote\r\n\t{}\r\n@media only screen and (max-width: 480px){\r\nbody, table, td, p, a, li, blockquote\r\n\t{}\r\n\r\n\t}\r\n.mcnPreviewText\r\n\t{display:none!important}\r\n.bodyCell\r\n\t{margin:0 auto;\r\n\tpadding:0;\r\n\twidth:100%}\r\n.ExternalClass, .ExternalClass p, .ExternalClass td, .ExternalClass div, .ExternalClass span, .ExternalClass font\r\n\t{line-height:100%}\r\n.ReadMsgBody\r\n\t{width:100%}\r\n.ExternalClass\r\n\t{width:100%}\r\na[x-apple-data-detectors]\r\n\t{color:inherit!important;\r\n\ttext-decoration:none!important;\r\n\tfont-size:inherit!important;\r\n\tfont-family:inherit!important;\r\n\tfont-weight:inherit!important;\r\n\tline-height:inherit!important}\r\nbody\r\n\t{height:100%;\r\n\tmargin:0;\r\n\tpadding:0;\r\n\twidth:100%;\r\n\tbackground:#ffffff}\r\np\r\n\t{margin:0;\r\n\tpadding:0}\r\ntable\r\n\t{border-collapse:collapse}\r\ntd, p, a\r\n\t{word-break:break-word}\r\nh1, h2, h3, h4, h5, h6\r\n\t{display:block;\r\n\tmargin:0;\r\n\tpadding:0}\r\nimg, a img\r\n\t{border:0;\r\n\theight:auto;\r\n\toutline:none;\r\n\ttext-decoration:none}\r\nli p\r\n\t{margin:0!important}\r\n.ProseMirror a\r\n\t{}\r\n@media only screen and (max-width: 640px){\r\n.mceClusterLayout td\r\n\t{padding:4px!important}\r\n\r\n\t}\r\n@media only screen and (max-width: 480px){\r\nbody\r\n\t{width:100%!important;\r\n\tmin-width:100%!important}\r\nbody.mobile-native\r\n\t{}\r\nbody.mobile-native.selection-allowed a, body.mobile-native.selection-allowed .ProseMirror\r\n\t{}\r\ncolgroup\r\n\t{display:none}\r\nimg\r\n\t{height:auto!important}\r\n.mceWidthContainer\r\n\t{max-width:660px!important}\r\n.mceColumn\r\n\t{display:block!important;\r\n\twidth:100%!important}\r\n.mceColumn-forceSpan\r\n\t{display:table-cell!important;\r\n\twidth:auto!important}\r\n.mceColumn-forceSpan .mceButton a\r\n\t{min-width:0!important}\r\n.mceBlockContainer\r\n\t{padding-right:16px!important;\r\n\tpadding-left:16px!important}\r\n.mceTextBlockContainer\r\n\t{padding-right:16px!important;\r\n\tpadding-left:16px!important}\r\n.mceBlockContainerE2E\r\n\t{padding-right:0px;\r\n\tpadding-left:0px}\r\n.mceSpacing-24\r\n\t{padding-right:16px!important;\r\n\tpadding-left:16px!important}\r\n.mceImage, .mceLogo\r\n\t{width:100%!important;\r\n\theight:auto!important}\r\n.mceFooterSection .mceText, .mceFooterSection .mceText p\r\n\t{font-size:16px!important;\r\n\tline-height:140%!important}\r\n\r\n\t}\r\ndiv[contenteditable=\"true\"]\r\n\t{outline:0}\r\n.mceImageBorder\r\n\t{display:inline-block}\r\n.mceImageBorder img\r\n\t{border:0!important}\r\nbody, #bodyTable\r\n\t{background-color:rgb(244,244,244)}\r\n.mceText, .mceLabel\r\n\t{font-family:\"Helvetica Neue\",Helvetica,Arial,Verdana,sans-serif}\r\n.mceText, .mceLabel\r\n\t{color:rgb(0,0,0)}\r\n.mceText p\r\n\t{margin-bottom:0px}\r\n.mceText label\r\n\t{margin-bottom:0px}\r\n.mceText input\r\n\t{margin-bottom:0px}\r\n.mceSpacing-12 .mceInput + .mceErrorMessage\r\n\t{}\r\n.mceText p\r\n\t{margin-bottom:0px}\r\n.mceText label\r\n\t{margin-bottom:0px}\r\n.mceText input\r\n\t{margin-bottom:0px}\r\n.mceSpacing-24 .mceInput + .mceErrorMessage\r\n\t{}\r\n.mceInput\r\n\t{background-color:transparent;\r\n\tborder:2px solid rgb(208,208,208);\r\n\twidth:60%;\r\n\tcolor:rgb(77,77,77);\r\n\tdisplay:block}\r\n.mceLabel > .mceInput\r\n\t{margin-bottom:0px;\r\n\tmargin-top:2px}\r\n.mceLabel\r\n\t{display:block}\r\n.mceText p\r\n\t{color:rgb(0,0,0);\r\n\tfont-family:\"Helvetica Neue\",Helvetica,Arial,Verdana,sans-serif;\r\n\tfont-size:16px;\r\n\tfont-weight:normal;\r\n\tline-height:150%;\r\n\ttext-align:center;\r\n\tdirection:ltr}\r\n.mceText a\r\n\t{color:rgb(0,0,0);\r\n\tfont-style:normal;\r\n\tfont-weight:normal;\r\n\ttext-decoration:underline;\r\n\tdirection:ltr}\r\n@media only screen and (max-width: 480px) {\r\n.mceText p\r\n\t{font-size:16px!important;\r\n\tline-height:150%!important}\r\n\r\n\t}\r\n@media only screen and (max-width: 480px) {\r\n.mceBlockContainer\r\n\t{padding-left:16px!important;\r\n\tpadding-right:16px!important}\r\n\r\n\t}\r\n#dataBlockId-5 p, #dataBlockId-5 h1, #dataBlockId-5 h2, #dataBlockId-5 h3, #dataBlockId-5 h4, #dataBlockId-5 ul\r\n\t{text-align:center}\r\n-->\r\n</style>
    </head><body><span class=\"mcnPreviewText\" style=\"display:none; font-size:0px; line-height:0px; max-height:0px; max-width:0px; opacity:0; overflow:hidden; visibility:hidden\">Bring Disney's Stitch to your next game night!</span> 
    <center><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\" style=\"background-color:rgb(244,244,244)\">
    <tbody><tr><td class=\"bodyCell\" align=\"center\" valign=\"top\"><table id=\"root\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">
    <tbody data-block-id=\"9\" class=\"mceWrapper\"><tr><td align=\"center\" valign=\"top\" class=\"mceWrapperOuter\">
    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"max-width:660px\">
    <tbody><tr><td class=\"mceWrapperInner\" valign=\"top\" style=\"background-color:#ffffff; background-position:center; background-repeat:no-repeat; background-size:cover\">
    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" data-block-id=\"8\"><
    tbody><tr class=\"mceRow\"><td valign=\"top\" style=\"background-position:center; background-repeat:no-repeat; background-size:cover\">
    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\">
    <tbody><tr><td class=\"mceColumn\" data-block-id=\"-4\" valign=\"top\" colspan=\"12\" width=\"100%\" style=\"padding-top:0; padding-bottom:0\">
    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\"><tbody><tr>
    <td class=\"mceBlockContainer\" align=\"center\" valign=\"top\" style=\"background-color:#bd5999; padding-top:12px; padding-bottom:12px; padding-right:12px; padding-left:12px\">
    <a href=\"https://Bicyclecards.us20.list-manage.com/track/click?u=65fc89b665574ef1048cd5f3b&amp;id=eae4a5dadf&amp;e=71f1a20f08\" target=\"_blank\" data-block-id=\"1\" style=\"display:block\">
    <span class=\"mceImageBorder\" style=\"border:0; vertical-align:top; margin:0\"><img width=\"296.22222222222223\" height=\"auto\" alt=\"logo\" src=\"https://mcusercontent.com/65fc89b665574ef1048cd5f3b/images/1fb91073-cf88-c535-de89-c5d21a1b512c.png\" class=\"mceLogo\" style=\"width:296.22222222222223px; height:auto; max-width:296.22222222222223px!important; display:block\"></span></a></td></tr><tr><td class=\"mceBlockContainerE2E\" align=\"full\" valign=\"top\" style=\"background-color:#bd5999; padding-top:0; padding-bottom:12px; padding-right:0; padding-left:0\">
    <a href=\"https://Bicyclecards.us20.list-manage.com/track/click?u=65fc89b665574ef1048cd5f3b&amp;id=50d3eb9863&amp;e=71f1a20f08\" target=\"_blank\" data-block-id=\"2\" style=\"display:block\"><span class=\"mceImageBorder\" style=\"border:0; vertical-align:top; margin:0\"><img width=\"660\" height=\"auto\" alt=\"celebrate 626 day, shop now. stitch happily holding a tropical drink\" src=\"https://mcusercontent.com/65fc89b665574ef1048cd5f3b/images/56986e21-2c16-81b6-acae-c3c06d60c51d.jpg\" class=\"mceImage\" style=\"width:660px; height:auto; max-width:794px!important; display:block\"></span></a></td></tr><tr>
    <td class=\"mceBlockContainerE2E\" align=\"full\" valign=\"top\" style=\"background-color:#bd5999; padding-top:0; padding-bottom:0; padding-right:0; padding-left:0\">
    <a href=\"https://Bicyclecards.us20.list-manage.com/track/click?u=65fc89b665574ef1048cd5f3b&amp;id=a65af318e7&amp;e=71f1a20f08\" target=\"_blank\" data-block-id=\"11\" style=\"display:block\"><span class=\"mceImageBorder\" style=\"border:0; vertical-align:top; margin:0\"><img width=\"660\" height=\"auto\" alt=\"626 day sweepstakes. today only purchase a stitch playing card deck and be entered to win 1 of 5 uncut stitch deck sheets. shop now\" src=\"https://mcusercontent.com/65fc89b665574ef1048cd5f3b/images/01014a7f-2ec6-b3c7-21f6-3c0ad3d42afb.jpg\" class=\"mceImage\" style=\"width:660px; height:auto; max-width:794px!important; display:block\"></span></a></td></tr>
    <tr><td valign=\"top\" style=\"padding-top:0; padding-bottom:0; padding-right:0; padding-left:0\"><table width=\"100%\" style=\"border:0; background-color:#bd5999; border-collapse:separate\"><tbody><tr><td class=\"mceTextBlockContainer\" style=\"padding-left:24px; padding-right:24px; padding-top:0; padding-bottom:0\"><div data-block-id=\"3\" class=\"mceText\" id=\"dataBlockId-3\" style=\"width:100%\"><p><br></p>
    <p style=\"line-height:100%\"><em><span style=\"color:#ffffff\">
    <span style=\"font-size:9px\">*626 Day Official Sweepstakes Rules - Winners will be selected randomly. No Purch. Nec. Open to legal US res. (incl. DC); 18+. Limit: 1 entry/order. &nbsp;Odds vary. All entries must be received on 626 Day, 6/26/2024, by 11:59 PM PST. To enter purchase a Disney Stitch deck </span></span><strong><span style=\"color:#ffffff\">
    <span style=\"font-size:9px\">or </span></span></strong><span style=\"color:#ffffff\">
    <span style=\"font-size:9px\">send an email to </span></span></em><a href=\"mailto:support@bicyclecards.com\" style=\"color:#ffffff\">
    <em><span style=\"font-size:9px\">support@bicyclecards.com</span></em></a><em>
    <span style=\"color:#ffffff\"><span style=\"font-size:9px\"> with your full name. Winners will be contacted via email by 7/12/2024. &nbsp;Please see </span></span></em>
    <a href=\"https://Bicyclecards.us20.list-manage.com/track/click?u=65fc89b665574ef1048cd5f3b&amp;id=77ba4a5f76&amp;e=71f1a20f08\" style=\"color:#ffffff\"><em>
    <span style=\"font-size:9px\">https://bicyclecards.com/terms-and-conditions/general-promotion-rules</span></em></a><em>
    <span style=\"color:#ffffff\"><span style=\"font-size:9px\">for more details on this promotion which apply to all Bicycle promotions.</span></span></em></p>
    <p class=\"last-child\"><br></p></div></td></tr></tbody></table></td></tr><tr>
    <td class=\"mceLayoutContainer\" valign=\"top\" style=\"background-color:#f4f4f4; padding-top:8px; padding-bottom:8px; padding-right:8px; padding-left:8px\">
    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" data-block-id=\"7\" id=\"section_dcafd9d131a8610ce570e2f24b64393c\" class=\"mceFooterSection\">
    <tbody><tr class=\"mceRow\">
    <td valign=\"top\" style=\"background-color:#f4f4f4; background-position:center; background-repeat:no-repeat; background-size:cover; padding-top:0px; padding-bottom:0px\">
    <table border=\"0\" cellpadding=\"0\" cellspacing=\"12\" width=\"100%\" role=\"presentation\"><tbody><tr><td class=\"mceColumn\" data-block-id=\"-3\" valign=\"top\" colspan=\"12\" width=\"100%\" style=\"padding-top:0; padding-bottom:0; margin-bottom:12px\">
    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\">
    <tbody><tr><td align=\"center\" valign=\"top\" style=\"padding-top:0; padding-bottom:0; padding-right:0; padding-left:0\">
    <table width=\"100%\" style=\"border:0; border-collapse:separate\"><tbody><tr>
    <td class=\"mceTextBlockContainer\" style=\"padding-left:16px; padding-right:16px; padding-top:12px; padding-bottom:12px\">
    <div data-block-id=\"5\" class=\"mceText\" id=\"dataBlockId-5\" style=\"display:inline-block; width:100%\"><p class=\"last-child\">
    <a href=\"https://mailchi.mp/bicyclecards.com/celebrate-626-day-with-stitch-bicycle?e=71f1a20f08\">
    <span style=\"font-size:11px\">View email in browser</span></a><span style=\"font-size:11px\">
    <br>The United States Playing Card Company · 300 Gap Way · Erlanger, KY 41018-3160 · USA <br></span>
    <a href=\"https://Bicyclecards.us20.list-manage.com/profile?u=65fc89b665574ef1048cd5f3b&amp;id=71d02a7e8d&amp;e=71f1a20f08&amp;c=3cae57729d\">
    <span style=\"font-size:11px\">update your preferences</span></a><span style=\"font-size:11px\"> or </span>
    <a href=\"https://Bicyclecards.us20.list-manage.com/unsubscribe?u=65fc89b665574ef1048cd5f3b&amp;id=71d02a7e8d&amp;t=b&amp;e=71f1a20f08&amp;c=3cae57729d\">
    <span style=\"font-size:11px\">unsubscribe</span></a></p></div></td></tr></tbody></table></td></tr><tr><td class=\"mceLayoutContainer\" align=\"center\" valign=\"top\">
    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" data-block-id=\"-2\"><tbody>
    <tr class=\"mceRow\"><td valign=\"top\" style=\"background-position:center; background-repeat:no-repeat; background-size:cover; padding-top:0px; padding-bottom:0px\">
    <table border=\"0\" cellpadding=\"0\" cellspacing=\"24\" width=\"100%\" role=\"presentation\">
    <tbody></tbody></table></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></td></tr>
    </tbody></table></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody>
    </table></td></tr></tbody></table></td></tr></tbody></table></center>
    <img src=\"https://Bicyclecards.us20.list-manage.com/track/open.php?u=65fc89b665574ef1048cd5f3b&amp;id=3cae57729d&amp;e=71f1a20f08\" height=\"1\" width=\"1\" alt=\"\"></body></html>
"""


const val rawBody1 =
    """<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body><div dir=\"ltr\">Boa tarde<div><br><div>Este email serve como teste</div></div><div><br></div><div>Cumprimentos</div><div>sender</div></div></body></html>"""
const val expectedCleanedContent1 = "Boa tarde Este email serve como teste Cumprimentos sender"

val testEmailEmpty = Email(
    Sender("1", "email1@test.com"),
    0,
    Sender("1", "email1@test.com"),
    "test",
    "Medium",
    hasAttachments = false,
    isRead = false,
    "email1@test.com",
    testAuthStr,
    ""
)

val testEmail = Email(
    Sender("1", "email1@test.com"),
    0,
    Sender("1", "email1@test.com"),
    "test",
    "Medium",
    hasAttachments = false,
    isRead = false,
    "email1@test.com",
    testAuthStr,
    rawBody1
)

val testEmailWithBadHeaders = Email(
    Sender("1", "email1@test.com"),
    0,
    Sender("1", "email1@test.com"),
    "test",
    "Medium",
    hasAttachments = false,
    isRead = false,
    "email1@test.com",
    rawAuthResults = testFailingAuthStr,
    rawBody1
)

val realPhishingEmail1 = Email(
    Sender("Stansted Airport College Enquiry", "admissions@harlow-college.ac.uk"),
    0,
    Sender("Stansted Airport College Enquiry", "admissions@harlow-college.ac.uk"),
    "Thanks for getting in touch\n",
    "normal",
    hasAttachments = false,
    isRead = false,
    "admissions@harlow-college.ac.uk",
    realPhishingHeaders1,
    realPhishingBody1
)

val realPromotionalEmail = Email(
    Sender("Bicycle", "info@bicyclecards.com"),
    5,
    Sender("Bicycle", "info@bicyclecards.com"),
    "Celebrate 626 Day with Stitch & Bicycle 🎉",
    "normal",
    hasAttachments = false,
    isRead = false,
    "bounce-mc.us20_116605254.14813282-71f1a20f08@mail113.suw111.mcdlv.net",
    realPromotionalEmailHeaders,
    realPromotionalEmailBody
)
