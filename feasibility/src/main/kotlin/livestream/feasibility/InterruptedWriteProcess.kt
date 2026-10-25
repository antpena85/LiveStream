package livestream.feasibility

import java.nio.file.Path

/** Child-process fault fixture for the isolated M2 storage probe. */
fun main(arguments: Array<String>) {
    require(arguments.size == 3 && arguments[2] in setOf(
        "interrupt-before-commit", "interrupt-reset-before-commit", "interrupt-after-commit",
    ))
    DurableCommandProbe(Path.of(arguments[0]), beforeCommit = {
        if (arguments[2] != "interrupt-after-commit") Runtime.getRuntime().halt(23)
    }).use { probe ->
        if (arguments[2] == "interrupt-reset-before-commit") {
            probe.reset(confirmed = true, expectedEpoch = 0)
        } else {
            val receipt = probe.recordPoint(arguments[1], expectedEpoch = 0)
            // Commit has returned, but the parent has not received the receipt.
            if (arguments[2] == "interrupt-after-commit") Runtime.getRuntime().halt(23)
            println(receipt)
        }
    }
}
