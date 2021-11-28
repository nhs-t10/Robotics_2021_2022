package net.coleh.autoautolanguageplugin.parse.syntaxhighlight;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;

import net.coleh.autoautolanguageplugin.AutoautoIcons;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import javax.swing.Icon;

public class AutoautoColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("OPERATOR", AutoautoSyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("LABEL", AutoautoSyntaxHighlighter.LABEL),
            new AttributesDescriptor("FUNCTION", AutoautoSyntaxHighlighter.FUNCTION),
            new AttributesDescriptor("VALUE", AutoautoSyntaxHighlighter.VALUE),
            new AttributesDescriptor("COMMA", AutoautoSyntaxHighlighter.COMMA),
            new AttributesDescriptor("SEMICOLON", AutoautoSyntaxHighlighter.SEMICOLON),
            new AttributesDescriptor("COMMENT", AutoautoSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("STATEMENT", AutoautoSyntaxHighlighter.STATEMENT),
            new AttributesDescriptor("BAD_CHARACTER", AutoautoSyntaxHighlighter.BAD_CHARACTER)
    };

    public Icon getIcon() {
        return AutoautoIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new AutoautoSyntaxHighlighter();
    }

    public String getDemoText() {
        return "#shooting:\n" +
                "    driveOmni(0.3,0,0), after 1550ticks next;\n" +
                "    driveOmni(0,0.3,0), after 70ticks next;\n" +
                "    let rings = 0, if(isSpecial(sensorOne)) let rings = 1, if(isSpecial(sensorFour)) let rings = 4, log(rings), after 0.5s next;\n";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Autoauto";
    }
}
