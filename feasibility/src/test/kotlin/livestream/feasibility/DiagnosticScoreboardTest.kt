package livestream.feasibility

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DiagnosticScoreboardTest {
    @Test
    fun scoringForRightTeamPreservesLeftScore() {
        val scoreboard = DiagnosticScoreboard()
        scoreboard.pointLeft()
        scoreboard.pointRight()
        scoreboard.pointRight()
        assertThat(scoreboard.display()).isEqualTo("ABC  1  |  2  XYZ")
    }

    @Test
    fun scoringForLeftTeamUpdatesOnlyLeftDisplayedScore() {
        val scoreboard = DiagnosticScoreboard()
        scoreboard.pointLeft()
        assertThat(scoreboard.display()).isEqualTo("ABC  1  |  0  XYZ")
    }
}