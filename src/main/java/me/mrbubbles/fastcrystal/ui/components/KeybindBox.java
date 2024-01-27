package me.mrbubbles.fastcrystal.ui.components;

import me.mrbubbles.fastcrystal.settings.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import static me.mrbubbles.fastcrystal.FastCrystal.mc;
import static net.minecraft.client.gui.DrawableHelper.fill;

public class KeybindBox implements FastCrystalGuiObj {

    private final KeybindSetting setting;
    private final int x;
    private final int y;
    private final int padding;
    private final int boxWidth;
    private final int boxHeight;
    private boolean listening;
    private int keybind;

    public KeybindBox(KeybindSetting setting, int x, int y, int boxHeight) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        listening = false;
        keybind = setting.getValue();
        this.padding = boxHeight / 4;
        this.boxWidth = boxHeight * 2;
        this.boxHeight = boxHeight;
    }

    @Override
    public void mouseScrolled(double mx, double my, float inc) {
    }

    @Override
    public void mouseClicked(double mx, double my) {
        if (isWithin(mx, my)) {
            listening = true;
        }
    }

    private String getKeyName(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return "none";
        String str = InputUtil.fromKeyCode(key, 0).getTranslationKey().toLowerCase();
        str = str.replace("KEY.KEYBOARD", "").replace(".", "");
        if (str.charAt(0) == ' ') str = str.substring(1);
        return str;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY) {

            mc.textRenderer.drawWithShadow(matrices, setting.getName(), x - mc.textRenderer.getWidth(setting.getName()) - padding, y + boxHeight / 2f - mc.textRenderer.fontHeight / 2f, -1);

            fill(matrices, x - 1, y - 1, x + boxWidth + 1, y + boxHeight + 1, -0x33333334);
            fill(matrices, x, y, x + boxWidth, y + boxHeight, -0x78EFEFF0);

            if (listening) {

                mc.textRenderer.drawWithShadow(matrices, "Press any key", x + boxWidth + padding, y + boxHeight / 2f - mc.textRenderer.fontHeight / 2f, -1);

                for (int i = GLFW.GLFW_KEY_SPACE; i <= GLFW.GLFW_KEY_LAST; i++) {
                    if (GLFW.glfwGetKey(mc.getWindow().getHandle(), i) == GLFW.GLFW_PRESS) {
                        if (i == GLFW.GLFW_KEY_ESCAPE) {
                            keybind = -1;
                            setting.setValue(-1);
                        } else {
                            keybind = i;
                            setting.setValue(i);
                        }
                        listening = false;
                        break;
                    }
                }
            } else {
                String keyName = GLFW.glfwGetKeyName(keybind, 0);

                if (keyName == null || keyName.isEmpty()) {
                    mc.textRenderer.drawWithShadow(matrices, getKeyName(keybind), x + boxWidth / 2f - mc.textRenderer.getWidth(String.valueOf(keybind)) / 2f, y + boxHeight / 2f - mc.textRenderer.fontHeight / 2f, -1);
                } else if (keybind == GLFW.GLFW_KEY_UNKNOWN) {
                    mc.textRenderer.drawWithShadow(matrices, "None", x + boxWidth / 2f - mc.textRenderer.getWidth("None") / 2f, y + boxHeight / 2f - mc.textRenderer.fontHeight / 2f, -1);
                } else {
                    mc.textRenderer.drawWithShadow(matrices, keyName, x + boxWidth / 2f - mc.textRenderer.getWidth(keyName) / 2f, y + boxHeight / 2f - mc.textRenderer.fontHeight / 2f, -1);
                }
                if (isWithin(mouseX, mouseY)) {
                    matrices.translate(0.0f, 0.0f, 1.0f);
                    fill(matrices, mouseX + 5, mouseY - 1, mouseX + 6 + mc.textRenderer.getWidth(setting.getDescription()), mouseY + 9, 0xEF000000);
                    mc.textRenderer.drawWithShadow(matrices, setting.getDescription(), mouseX + 6, mouseY, -1);
                    matrices.translate(0.0f, 0.0f, -1.0f);
                }
            }
    }

    @Override
    public boolean isWithin(double mouseX, double mouseY) {
        return mouseX > x && mouseY > y && mouseX < x + boxWidth && mouseY < y + boxHeight;
    }
}