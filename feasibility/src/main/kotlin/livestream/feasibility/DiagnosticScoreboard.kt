package livestream.feasibility

/** Local-recording fixture only; not a volleyball match implementation. */
class DiagnosticScoreboard {
    private var left = 0
    private var right = 0
    fun pointLeft() { left++ }
    fun pointRight() { right++ }
    fun display(): String = "ABC  $left  |  $right  XYZ"
}