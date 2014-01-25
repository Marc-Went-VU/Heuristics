package tiling.own;

import java.util.Comparator;

public class FieldSetComparator implements Comparator<FieldSet>
{
	public int compare(FieldSet first, FieldSet second)
	{
		return first.compareTo(second);
	}
}
