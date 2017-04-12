package nosi.core.gui.fields;

public interface Field {
	public String getTagName();
	public void setTagName(String tag_name);
	public String getName();
	public void setName(String name);
	public String getMax_length();
	public void setMaxLength(String max_length);
	public String getRequired();
	public void setRequired(String required);
	public String getChange();
	public void setChange(String change);
	public Object getValue();
	public void setValue(Object value);
	public void setValue(int value);
	public void setValue(float value);
	public void setValue(double value);
	public void setVisible(boolean visible);
	public boolean getVisible();
	public void setValue(Object name,Object value);
}
