package net.fabricmc.example;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.minecraft.client.gui.screen.Screen;

import javax.security.auth.login.Configuration;
import java.util.Optional;
import java.util.function.Supplier;

@Config(name = "configtest")
public class ModConfig implements ConfigData, ModMenuApi {

    @ConfigEntry.Gui.PrefixText
    boolean toggleA = true;
    boolean toggleB = false;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(ModConfig.class, screen).get();
    }

    @Override
    public String getModId() {
        return "configtest";
    }
}
