package org.aksw.verilinks.core.client.gui;

import org.aksw.verilinks.core.shared.msg.Task;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskTableColumn extends VerticalPanel{

  private Task task;
  
  public TaskTableColumn(Task task){
    super();
    this.task=task;
    initGUI();
  }

  public TaskTableColumn() {
    super();
    HTML subject = new HTML("Subject");
    subject.addStyleName("taskTableColumn_head");
    HTML object = new HTML("Object");
    object.addStyleName("taskTableColumn_head");
    HTML file = new HTML("File");
    file.addStyleName("taskTableColumn_head");
    add(subject);
    add(object);
    add(file);
  }

  public TaskTableColumn(String string) {
    super();
    HTML value = new HTML(string);
    value.addStyleName("taskTableColumn_head");
    add(value);
  }

  public void initGUI() {
    HTML subject = new HTML(task.getSubject());
    subject.addStyleName("taskTableColumn_text");
    HTML object = new HTML(task.getObject());
    object.addStyleName("taskTableColumn_text");
    HTML file = new HTML(task.getFile());
    file.addStyleName("taskTableColumn_text");
    add(subject);
    add(object);
    add(file);
    
    
  }

  public void addValue(HTML html) {
    html.setStyleName("taskTableColumn_text");
    add(html);
    System.out.println("add Value "+html.getText());
  }
}
