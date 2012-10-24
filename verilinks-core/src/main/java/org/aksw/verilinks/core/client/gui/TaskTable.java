package org.aksw.verilinks.core.client.gui;

import java.util.ArrayList;

import org.aksw.verilinks.core.shared.msg.Task;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskTable extends HorizontalPanel{

  private ArrayList<Task> tasks;
  
  public TaskTable(ArrayList<Task> tasks){
    super();
    this.tasks=tasks;
    initGUI();
  }

  public void initGUI() {
    System.out.println("INIT GUI ");
    TaskTableColumn subject = new TaskTableColumn("Subject");
    subject.setStyleName("taskTableColumn");
    TaskTableColumn object = new TaskTableColumn("Object");
    object.setStyleName("taskTableColumn");
    TaskTableColumn predicate = new TaskTableColumn("Predicate");
    predicate.setStyleName("taskTableColumn");
    TaskTableColumn description = new TaskTableColumn("Description");
    description.setStyleName("taskTableColumn");
    TaskTableColumn difficulty = new TaskTableColumn("Difficulty");
    difficulty.setStyleName("taskTableColumn");
    TaskTableColumn file = new TaskTableColumn("File");
    file.setStyleName("taskTableColumn");
    TaskTableColumn linkset = new TaskTableColumn("Linkset");
    linkset.setStyleName("taskTableColumn");
    
    for(int i=0;i<tasks.size();i++){
      subject.addValue(new HTML(tasks.get(i).getSubject()));
      object.addValue(new HTML(tasks.get(i).getObject()));
      predicate.addValue(new HTML(tasks.get(i).getPredicate()));
      description.addValue(new HTML(tasks.get(i).getDescription()));
      difficulty.addValue(new HTML(tasks.get(i).getDifficulty()));
      file.addValue(new HTML(tasks.get(i).getFile()));
      linkset.addValue(new HTML(tasks.get(i).getLinkset()));
    }
    add(subject);
    add(object);
    add(predicate);
    add(description);
    add(difficulty);
    add(file);
    add(linkset);
  }
}
