package com.github.hellsingdarge.compactstatuseffects.drawModules

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectUtil

class NoSprite(matrixStack: MatrixStack, x: Int, y: Int, effects: Iterable<StatusEffectInstance>):
        DrawModule(matrixStack, x, y, effects)
{
    private val config = modConfig.noSpriteConfig
    private val xOffset = 99 + modConfig.margin
    private val xIncrement = 99
    private val yIncrement = 31
    private val maxNum = config.maxEffectsNumber

    override fun drawBackground()
    {
        textureManager.bindTexture(backgroundTexture)
        var i = x
        var j = y

        repeat(effects.count()) { index ->
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F)
            drawTexture(matrixStack, i - xOffset, j, 0, 60, 100, 31)

            i = x - ((index + 1) / maxNum) * xIncrement
            j += yIncrement
            if ((index + 1) % maxNum == 0) j = y
        }
    }

    override fun drawDescription()
    {
        var i = y
        var j = x

        effects.forEachIndexed { index, instance ->
            var effectName = I18n.translate(instance.effectType.translationKey, *arrayOfNulls(0))

            if (instance.amplifier in 1..9)
            {
                effectName = effectName + ' ' + I18n.translate("enchantment.level." + (instance.amplifier + 1), *arrayOfNulls(0))
            }

            Util.drawLeftAlign(matrixStack, effectName, j + 5f - xOffset, i + 6f, 0xFFFFFF, true)
            val duration = StatusEffectUtil.durationToString(instance, 1.0f)

            if (instance.isPermanent)
            {
                Util.drawLeftAlign(matrixStack, "∞", j + 5f - xOffset, i + 16f, 0x7F7F7F, true)
            }
            else
            {
                Util.drawLeftAlign(matrixStack, duration, j + 5f - xOffset, i + 16f, 0x7F7F7F, true)
            }

            i += yIncrement
            j = x - ((index + 1) / maxNum) * xIncrement
            if ((index + 1) % maxNum == 0) i = y
        }
    }
}