package org.aksw.verilinks.core.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aksw.verilinks.core.shared.msg.Task;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TaskPanel extends VerticalPanel{
  
  private ArrayList<Task> tasks;
  private Button quitBtn;
  private Button okBtn;
  private HTML result;
  
  public TaskPanel(ArrayList<Task> tasks){
    this.tasks=tasks;
    
    initGUI();
  }
  
  public void initGUI() {
    /*
    CellTable<Task> table = new CellTable<Task>();
    
    // Subject
    TextColumn<Task> subjectColumn = new TextColumn<Task>() {
      
      @Override
      public String getValue(Task object) {
        // TODO Auto-generated method stub
        return object.getSubject();
      }
    };
    
    // Object
    TextColumn<Task> objectColumn = new TextColumn<Task>() {
      
      @Override
      public String getValue(Task object) {
        // TODO Auto-generated method stub
        return object.getObject();
      }
    };
    
    // File
    TextColumn<Task> fileColumn = new TextColumn<Task>() {
      
      @Override
      public String getValue(Task object) {
        // TODO Auto-generated method stub
        return object.getFile();
      }
    };
    
    table.addColumn(subjectColumn,"subject");
    table.addColumn(objectColumn,"object");
    table.addColumn(fileColumn,"file");
    //table.setRowCount(tasksData.size(), true);
    table.setRowData(tasks);
    //table.setSize("460px", "196px");
    */
    TaskTable table= new TaskTable(tasks);
    table.addStyleName("taskPanel_table");
    quitBtn = new Button("Quit");
    //quitBtn.addStyleName("adminPanel_quitBtn");
    quitBtn.addStyleName("myButton");
    
    
    //System.out.println("Task Size: "+tasks.size());
    if (tasks.isEmpty()) {
      okBtn = new Button("No Tasks to perform");
      okBtn.setEnabled(false);
    }
    else
      okBtn = new Button("Perform Tasks");
    //okBtn.setStyleName("adminPanel_addBtn");
    okBtn.addStyleName("myButton");
    
    result = new HTML("");
    result.addStyleName("TaskPanel_Result");
    
    
    FlowPanel bottomPanel = new FlowPanel();
    bottomPanel.add(quitBtn);
    bottomPanel.add(okBtn);
    bottomPanel.add(result);
    
    HTML headerText = new HTML("Pending tasks");
    headerText.addStyleName("adminPanel_headerText");
    HorizontalPanel headerPanel = new HorizontalPanel();
    headerPanel.add(headerText);
    headerPanel.add(quitBtn);
    
    headerPanel.addStyleName("adminPanel_header");
    
    this.add(headerPanel);
    this.add(table);
    this.add(bottomPanel);
//    this.setStyleName("noBorder");
    //this.add(okBtn);
    // Debug: size off task -> how many tasks
    //System.out.println("Size: "+tasks.size());
  }

  public void setQuitClickHandler(ClickHandler clickHandler) {
    this.quitBtn.addClickHandler(clickHandler);
    
  }
  
  public void setOkClickHandler(ClickHandler clickHandler) {
    this.okBtn.addClickHandler(clickHandler);
    
  }
  
  public void disableOkButton() {
    this.okBtn.setEnabled(false);
  }
  
  
  public void enableQuitButton(boolean b){
    this.quitBtn.setEnabled(b);
  }
  
  public void setResult(String res) {
    this.result.setText(res);
  }
}
