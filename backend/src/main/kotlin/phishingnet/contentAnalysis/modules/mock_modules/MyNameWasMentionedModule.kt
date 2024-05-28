package phishingnet.contentAnalysis.modules.mock_modules

import phishingnet.contentAnalysis.models.AnalysisModule
import phishingnet.contentAnalysis.models.Email
import phishingnet.contentAnalysis.models.warnings.WarningLog
import phishingnet.contentAnalysis.models.warnings.Warning
import phishingnet.contentAnalysis.models.warnings.toOccurrences

class MyNameWasMentionedModule: AnalysisModule {
    override val name = "My name was mentioned somewhere"
    override var active = true

    override fun process(email: Email): WarningLog {
        val warning = Warning.NAME_MENTIONED
        val warningLog = WarningLog(warning)
        val myName = "Manuel"

        val words = email.body.split("\\W+".toRegex())
        val counter = words.count { it.equals(myName, ignoreCase = true) }.toOccurrences()

        warningLog[warning] = counter
        return warningLog
    }
}