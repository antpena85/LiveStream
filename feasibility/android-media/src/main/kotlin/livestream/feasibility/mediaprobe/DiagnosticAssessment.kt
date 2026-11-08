package livestream.feasibility.mediaprobe

import livestream.feasibility.ProbeSnapshot

data class DiagnosticAssessment(val display: String, val snapshot: ProbeSnapshot, val fits: Boolean)