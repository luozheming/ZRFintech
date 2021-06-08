package com.enums;

public enum VoicePrompt {
    COUNTDOWN("倒计时提示音", "https://zrfintech-dev.oa.cmbchina.biz/applet/eGkw4gE3xT.txt");

    private String voicePromptDesc;
    private String voicePromptPath;

    VoicePrompt(String voicePromptDesc, String voicePromptPath) {
        this.voicePromptDesc = voicePromptDesc;
        this.voicePromptPath = voicePromptPath;
    }

    public String getVoicePromptDesc() {
        return this.voicePromptDesc;
    }

    public String getVoicePromptPath() {
        return voicePromptPath;
    }

    public static String getVoicePromptPath(String voicePromptDesc) {
        String voicePromptPath = "";
        for (VoicePrompt voicePrompt : VoicePrompt.values()) {
            if (voicePromptDesc == voicePrompt.getVoicePromptDesc()) {
                voicePromptPath = voicePrompt.getVoicePromptPath();
            }
        }
        return voicePromptPath;
    }
}
