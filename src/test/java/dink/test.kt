package dink

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.provider.ValueSource
import dinkplugin.DinkPlugin
import java.util.stream.Stream

class Matchers {
    @ParameterizedTest(name = "Slayer task completion message should trigger {0}")
    @ArgumentsSource(SlayerTaskProvider::class)
    fun `Slayer task completion regex finds match`(message: String, task: String) {
        val matcher = DinkPlugin.SLAYER_TASK_REGEX.matcher(message)
        assertTrue(matcher.find())
        assertEquals(task, matcher.group("task"))
    }

    @ParameterizedTest(name = "Slayer task completion message should trigger {0}")
    @ValueSource(strings = [
        "Forsen: forsen",
        "You're assigned to kill kalphite; only 3 more to go.",
        "You've completed 234 tasks and received 15 points, giving you a total of 801; return to a Slayer master.",
    ])
    fun `Slayer task completion regex does not match`(message: String) {
        val matcher = DinkPlugin.SLAYER_TASK_REGEX.matcher(message)
        assertFalse(matcher.find())
    }

    private class SlayerTaskProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
            Arguments.of("You have completed your task! You killed 125 Kalphite. You gained 11,150 xp.", "125 Kalphite"),
            Arguments.of("You have completed your task! You killed 7 Ankous. You gained 75 xp.", "7 Ankous"),
            Arguments.of("You have completed your task! You killed 134 Abyssal demons. You gained 75 xp.", "134 Abyssal demons"),
            Arguments.of("You have completed your task! You killed 134 Fossil Island Wyverns. You gained 75 xp.", "134 Fossil Island Wyverns"),
            Arguments.of("You have completed your task! You killed 31 Kree'Arra. You gained 75 xp.", "31 Kree'Arra"),
            Arguments.of("You have completed your task! You killed 31 TzKal-Zuk. You gained 75 xp.", "31 TzKal-Zuk")
        )
    }
}