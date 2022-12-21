package com.learnspigot.bot.command

import com.learnspigot.bot.LearnSpigotBot
import com.learnspigot.bot.LearnSpigotBot.Companion.findUserProfile
import com.learnspigot.bot.LearnSpigotBot.Companion.replyEmbed
import com.learnspigot.bot.entity.UserProfile
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import dev.minn.jda.ktx.events.onCommand
import dev.minn.jda.ktx.interactions.commands.restrict
import dev.minn.jda.ktx.interactions.commands.upsertCommand
import dev.morphia.Datastore

class ToggleCommand(private val guild: Guild, private val bot: JDA, private val datastore: Datastore) {
    fun toggleCommand() {
        guild.upsertCommand("toggleping", "Toggle whether or not you want to be pinged when you got a reputation") {
            restrict(guild = true)
            bot.onCommand("toggleping") {
                val profile: UserProfile = datastore.findUserProfile(it.user.id)
                val ping: Boolean = profile.reputationPing
                profile.changeRepuationToPing()
                datastore.save(profile)
                it.replyEmbed({
                    title = "Success"
                    description = "You will ${if (!ping) "**be**" else "**not be**"} pinged when receiving reputation"
                    color = LearnSpigotBot.EMBED_COLOR
                }).queue()
            }
        }.queue()
    }
}
