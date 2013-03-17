package android.course.project.sos.domain;

import android.widget.CheckBox;
import android.widget.TextView;

public class ContactView {
	private CheckBox checkBox ;
    private TextView textView ;
    
    public ContactView() {

    }
    public ContactView( TextView textView) {
    	this.textView = textView ;
    }

    public ContactView( TextView textView, CheckBox checkBox ) {
    	this.checkBox = checkBox ;
    	this.textView = textView ;
    }
    public CheckBox getCheckBox() {
      return checkBox;
    }
    public void setCheckBox(CheckBox checkBox) {
      this.checkBox = checkBox;
    }
    public TextView getTextView() {
      return textView;
    }
    public void setTextView(TextView textView) {
      this.textView = textView;
    }
}
