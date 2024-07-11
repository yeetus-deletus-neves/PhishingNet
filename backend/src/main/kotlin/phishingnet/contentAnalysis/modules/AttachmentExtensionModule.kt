package phishingnet.contentAnalysis.modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.WarningLog

class AttachmentExtensionModule: AnalysisModule{
    override val name = "Módulo Análise extensões de ficheiros"

    override fun process(email: Email): WarningLog {

        val abc = arrayOf("exe", "pdf" ,"dll", "xls", "lnk", "ps1", "jar", "doc", "xlsb", "vbs", "xlsm", "ppt", "scr", "rtf", "bat")
        val warningLog = WarningLog(Warning.FILE_ATTACHED_CAN_BE_DANGEROUS)

        for (file in email.attachments){
            val index = file.lastIndexOf('.')
            if (index == -1) continue

            if (abc.contains(file.substring(index + 1))) warningLog.incrementOccurrences(Warning.FILE_ATTACHED_CAN_BE_DANGEROUS)
        }


        return warningLog
    }

}
