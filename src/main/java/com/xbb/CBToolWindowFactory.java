package com.xbb;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class CBToolWindowFactory implements ToolWindowFactory {

    @Override
    public boolean isApplicable(@NotNull Project project) {
        return ToolWindowFactory.super.isApplicable(project);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        EastMoneyWindow eastMoneyWindow = new EastMoneyWindow();
        JiSiLuWindow jiSiLuWindow = new JiSiLuWindow();
        CbPreWindow cbPreWindow = new CbPreWindow();
        IndexPerformanceWindow indexPerformanceWindow = new IndexPerformanceWindow();
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content t = contentFactory.createContent(jiSiLuWindow.panel, "完整", false);
        Content e = contentFactory.createContent(eastMoneyWindow.panel, "精简", false);
        Content d = contentFactory.createContent(cbPreWindow.panel, "待发转债", false);
        Content f = contentFactory.createContent(indexPerformanceWindow.panel, "指数", false);
        toolWindow.getContentManager().addContent(t);
        toolWindow.getContentManager().addContent(e);
        toolWindow.getContentManager().addContent(d);
        toolWindow.getContentManager().addContent(f);
        toolWindow.getContentManager().setSelectedContent(t);
    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        ToolWindowFactory.super.init(toolWindow);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return ToolWindowFactory.super.shouldBeAvailable(project);
    }

}

