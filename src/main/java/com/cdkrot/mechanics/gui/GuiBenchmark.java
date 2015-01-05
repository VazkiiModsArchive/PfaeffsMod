package com.cdkrot.mechanics.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

import com.cdkrot.mechanics.Mechanics;
import com.cdkrot.mechanics.net.GamePosition;
import com.cdkrot.mechanics.net.PacketBenchmarkIO;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBenchmark extends GuiScreen {
    private GuiTextField text;
    private final GamePosition benchmark;
    private GuiButton done;
    private String s;

    public GuiBenchmark(GamePosition pos, String s) {
        this.benchmark = pos;
        this.s = s;
    }

    @Override
    public void updateScreen() {
        this.text.updateCursorCounter();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(done = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Done"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
        this.text = new GuiTextField(this.fontRendererObj, this.width / 2 - 150, 60, 300, 20);
        this.text.setMaxStringLength(300);
        this.text.setFocused(true);
        this.text.setText(s);
        s = null;
        this.done.enabled = text.getText().trim().length() != 0;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton btn) {
        if (btn.enabled) {
            if (btn.id == 1)// close-cancel
                this.mc.displayGuiScreen(null);
            else if (btn.id == 0) {
                try {
                    PacketBenchmarkIO packet = new PacketBenchmarkIO();
                    packet.pos = benchmark;
                    packet.text = text.getText();
                    Mechanics.networkHandler.sendToServer(packet);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                this.mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    protected void keyTyped(char p1, int p2) {
        // TODO: figure out what is p1 and p2 here
        // source: commandblock.
        this.text.textboxKeyTyped(p1, p2);
        this.done.enabled = this.text.getText().trim().length() != 0;

        if (p1 != 28 && p2 == 1)
            this.mc.displayGuiScreen(null);// cancel;
        else
            this.actionPerformed(this.done);
    }

    @Override
    protected void mouseClicked(int p1, int p2, int p3) {
        super.mouseClicked(p1, p2, p3);
        this.text.mouseClicked(p1, p2, p3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int p1, int p2, float p3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Benchmark", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "Set text:", this.width / 2 - 150, 47, 10526880);
        this.text.drawTextBox();
        super.drawScreen(p1, p2, p3);
    }
}
