package pers.cy.iris.client.cli;

import pers.cy.iris.client.cli.exception.AlreadySelectedException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in 16:51 17/3/13
 * @Destription: 一组相互排斥的选项
 */
public class OptionGroup implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 存储选项 */
	private Map<String/*option唯一标识符*/,Option> optionMap = new HashMap<String, Option>();

	/** 被选定的选项 */
	private String selected;

	/** 此组是否必须的 */
	private boolean required;

	/**
	 * Add the specified <code>Option</code> to this group.
	 *
	 * @param option the option to add to this group
	 * @return this option group with the option added
	 */
	public OptionGroup addOption(Option option)
	{
		// key   - option name
		// value - the option
		optionMap.put(option.getKey(), option);

		return this;
	}

	/**
	 * @return the names of the options in this group as a
	 * <code>Collection</code>
	 */
	public Collection getNames()
	{
		// the key set is the collection of names
		return optionMap.keySet();
	}

	/**
	 * @return the options in this group as a <code>Collection</code>
	 */
	public Collection getOptions()
	{
		// the values are the collection of options
		return optionMap.values();
	}

	/**
	 * Set the selected option of this group to <code>name</code>.
	 *
	 * @param option the option that is selected
	 * @throws AlreadySelectedException if an option from this group has
	 * already been selected.
	 */
	public void setSelected(Option option) throws AlreadySelectedException
	{
		// if no option has already been selected or the
		// same option is being reselected then set the
		// selected member variable
		if (selected == null || selected.equals(option.getOpt()))
		{
			selected = option.getOpt();
		}
		else
		{
			throw new AlreadySelectedException(this, option);
		}
	}

	/**
	 * @return the selected option name
	 */
	public String getSelected()
	{
		return selected;
	}

	/**
	 * @param required specifies if this group is required
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}

	/**
	 * Returns whether this option group is required.
	 *
	 * @return whether this option group is required
	 */
	public boolean isRequired()
	{
		return required;
	}

	/**
	 * Returns the stringified version of this OptionGroup.
	 *
	 * @return the stringified representation of this group
	 */
	public String toString()
	{
		StringBuffer buff = new StringBuffer();

		Iterator iter = getOptions().iterator();

		buff.append("[");

		while (iter.hasNext())
		{
			Option option = (Option) iter.next();

			if (option.getOpt() != null)
			{
				buff.append("-");
				buff.append(option.getOpt());
			}
			else
			{
				buff.append("--");
				buff.append(option.getLongOpt());
			}

			buff.append(" ");
			buff.append(option.getDescription());

			if (iter.hasNext())
			{
				buff.append(", ");
			}
		}

		buff.append("]");

		return buff.toString();
	}
}
