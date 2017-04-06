package pers.cy.iris.client.cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 命令行选项.
 * 此类维护一个选项的缩写名称,完整名称,一个表示是否必须的参数以及此选项的自我描述
 */
public class Option implements Cloneable, Serializable
{

	private static final long serialVersionUID = 8724925342107253167L;

	/** constant that specifies the number of argument values has not been specified */
	public static final int UNINITIALIZED = -1;

	/** constant that specifies the number of argument values is infinite */
	public static final int UNLIMITED_VALUES = -2;

	/**缩写名称*/
	private String opt;

	/** 完整名称 */
	private String longOpt;

	/** 此选项参数名 */
	private String argName = "arg";

	/** 选项描述 */
	private String description;

	/** 选项是否必须存在 */
	private boolean required;

	/** specifies whether the argument value of this Option is optional */
	private boolean optionalArg;

	/** 拥有的参数数量 */
	private int numberOfArgs = UNINITIALIZED;

	/** 选项类型 */
	private Object type;

	/** 保存参数值 **/
	private List values = new ArrayList();

	/** 值分隔符 */
	private char valuesep;

	/**
	 * 构造选项.
	 *
	 * @param opt 缩略名称
	 * @param description 描述选项功能
	 *
	 * @throws IllegalArgumentException 参数不合法.
	 */
	public Option(String opt, String description) throws IllegalArgumentException
	{
		this(opt, null, false, description);
	}

	/**
	 * 构造选项.
	 *
	 * @param opt 缩略名称
	 * @param hasArg 选项是否有参数
	 * @param description 描述选项功能
	 *
	 * @throws IllegalArgumentException 参数不合法.
	 */
	public Option(String opt, boolean hasArg, String description) throws IllegalArgumentException
	{
		this(opt, null, hasArg, description);
	}

	/**
	 * 构造选项.
	 *
	 * @param opt 缩略名称
	 * @param longOpt 完整名称
	 * @param hasArg 选项是否有参数
	 * @param description 描述选项功能
	 *
	 * @throws IllegalArgumentException 参数不合法.
	 */
	public Option(String opt, String longOpt, boolean hasArg, String description)
			throws IllegalArgumentException
	{
		// 验证缩写是否合法
		OptionValidator.validateOption(opt);

		this.opt = opt;
		this.longOpt = longOpt;

		// 有参数则设置参数为1
		if (hasArg)
		{
			this.numberOfArgs = 1;
		}

		this.description = description;
	}

	/**
	 * Returns the id of this Option.  This is only set when the
	 * Option shortOpt is a single character.  This is used for switch
	 * statements.
	 *
	 * @return the id of this Option
	 */
	public int getId()
	{
		return getKey().charAt(0);
	}

	/**
	 * 选项的唯一标识符
	 * @return
	 */
	public String getKey()
	{
		// if 'opt' is null, then it is a 'long' option
		if (opt == null)
		{
			return longOpt;
		}

		return opt;
	}

	/**
	 * 获取选项检索名.
	 *
	 * @return
	 */
	public String getOpt()
	{
		return opt;
	}

	/**
	 * 获取选项类型
	 *
	 * @return
	 */
	public Object getType()
	{
		return type;
	}

	/**
	 * 设置选项类型
	 *
	 * @param type
	 */
	public void setType(Object type)
	{
		this.type = type;
	}

	/**
	 * 获取选项完整名
	 *
	 * @return
	 */
	public String getLongOpt()
	{
		return longOpt;
	}

	/**
	 * 设置选项完整名
	 *
	 * @param longOpt
	 */
	public void setLongOpt(String longOpt)
	{
		this.longOpt = longOpt;
	}

	/**
	 * 设置选项是否有可选的参数
	 *
	 * @param optionalArg specifies whether the Option can have
	 * an optional argument.
	 */
	public void setOptionalArg(boolean optionalArg)
	{
		this.optionalArg = optionalArg;
	}

	/**
	 * @return 获取选项是否有可选的参数
	 */
	public boolean hasOptionalArg()
	{
		return optionalArg;
	}

	/**
	 * 查询选项是否存在完整名
	 *
	 * @return
	 */
	public boolean hasLongOpt()
	{
		return longOpt != null;
	}

	/**
	 * 查询选项是否需要参数
	 *
	 * @return boolean flag indicating if an argument is required
	 */
	public boolean hasArg()
	{
		return numberOfArgs > 0 || numberOfArgs == UNLIMITED_VALUES;
	}

	/**
	 * 获取选项的描述
	 *
	 * @return The string description of this option
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets the self-documenting description of this Option
	 *
	 * @param description The description of this option
	 * @since 1.1
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Query to see if this Option requires an argument
	 *
	 * @return boolean flag indicating if an argument is required
	 */
	public boolean isRequired()
	{
		return required;
	}

	/**
	 * Sets whether this Option is mandatory.
	 *
	 * @param required specifies whether this Option is mandatory
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}

	/**
	 * Sets the display name for the argument value.
	 *
	 * @param argName the display name for the argument value.
	 */
	public void setArgName(String argName)
	{
		this.argName = argName;
	}

	/**
	 * Gets the display name for the argument value.
	 *
	 * @return the display name for the argument value.
	 */
	public String getArgName()
	{
		return argName;
	}

	/**
	 * Returns whether the display name for the argument value
	 * has been set.
	 *
	 * @return if the display name for the argument value has been
	 * set.
	 */
	public boolean hasArgName()
	{
		return argName != null && argName.length() > 0;
	}

	/**
	 * Query to see if this Option can take many values.
	 *
	 * @return boolean flag indicating if multiple values are allowed
	 */
	public boolean hasArgs()
	{
		return numberOfArgs > 1 || numberOfArgs == UNLIMITED_VALUES;
	}

	/**
	 * 设置选项能带的参数个数
	 *
	 * @param num
	 */
	public void setArgs(int num)
	{
		this.numberOfArgs = num;
	}

	/**
	 * Sets the value separator.  For example if the argument value
	 * was a Java property, the value separator would be '='.
	 *
	 * @param sep The value separator.
	 */
	public void setValueSeparator(char sep)
	{
		this.valuesep = sep;
	}

	/**
	 * Returns the value separator character.
	 *
	 * @return the value separator character.
	 */
	public char getValueSeparator()
	{
		return valuesep;
	}

	/**
	 * Return whether this Option has specified a value separator.
	 *
	 * @return whether this Option has specified a value separator.
	 * @since 1.1
	 */
	public boolean hasValueSeparator()
	{
		return valuesep > 0;
	}

	/**
	 * Returns the number of argument values this Option can take.
	 *
	 * @return num the number of argument values
	 */
	public int getArgs()
	{
		return numberOfArgs;
	}

	/**
	 * Adds the specified value to this Option.
	 *
	 * @param value is a/the value of this Option
	 */
	public void addValueForProcessing(String value)
	{
		switch (numberOfArgs)
		{
			case UNINITIALIZED:
				throw new RuntimeException("NO_ARGS_ALLOWED");

			default:
				processValue(value);
		}
	}

	/**
	 * Processes the value.  If this Option has a value separator
	 * the value will have to be parsed into individual tokens.  When
	 * n-1 tokens have been processed and there are more value separators
	 * in the value, parsing is ceased and the remaining characters are
	 * added as a single token.
	 *
	 * @param value The String to be processed.
	 *
	 * @since 1.0.1
	 */
	private void processValue(String value)
	{
		// this Option has a separator character
		if (hasValueSeparator())
		{
			// get the separator character
			char sep = getValueSeparator();

			// store the index for the value separator
			int index = value.indexOf(sep);

			// while there are more value separators
			while (index != -1)
			{
				// next value to be added
				if (values.size() == (numberOfArgs - 1))
				{
					break;
				}

				// store
				add(value.substring(0, index));

				// parse
				value = value.substring(index + 1);

				// get new index
				index = value.indexOf(sep);
			}
		}

		// store the actual value or the last value that has been parsed
		add(value);
	}

	/**
	 * Add the value to this Option.  If the number of arguments
	 * is greater than zero and there is enough space in the list then
	 * add the value.  Otherwise, throw a runtime exception.
	 *
	 * @param value The value to be added to this Option
	 *
	 * @since 1.0.1
	 */
	private void add(String value)
	{
		if ((numberOfArgs > 0) && (values.size() > (numberOfArgs - 1)))
		{
			throw new RuntimeException("Cannot add value, list full.");
		}

		// store value
		values.add(value);
	}

	/**
	 * Returns the specified value of this Option or
	 * <code>null</code> if there is no value.
	 *
	 * @return the value/first value of this Option or
	 * <code>null</code> if there is no value.
	 */
	public String getValue()
	{
		return hasNoValues() ? null : (String) values.get(0);
	}

	/**
	 * Returns the specified value of this Option or
	 * <code>null</code> if there is no value.
	 *
	 * @param index The index of the value to be returned.
	 *
	 * @return the specified value of this Option or
	 * <code>null</code> if there is no value.
	 *
	 * @throws IndexOutOfBoundsException if index is less than 1
	 * or greater than the number of the values for this Option.
	 */
	public String getValue(int index) throws IndexOutOfBoundsException
	{
		return hasNoValues() ? null : (String) values.get(index);
	}

	/**
	 * Returns the value/first value of this Option or the
	 * <code>defaultValue</code> if there is no value.
	 *
	 * @param defaultValue The value to be returned if ther
	 * is no value.
	 *
	 * @return the value/first value of this Option or the
	 * <code>defaultValue</code> if there are no values.
	 */
	public String getValue(String defaultValue)
	{
		String value = getValue();

		return (value != null) ? value : defaultValue;
	}

	/**
	 * Return the values of this Option as a String array
	 * or null if there are no values
	 *
	 * @return the values of this Option as a String array
	 * or null if there are no values
	 */
	public String[] getValues()
	{
		return hasNoValues() ? null : (String[]) values.toArray(new String[values.size()]);
	}

	/**
	 * @return the values of this Option as a List
	 * or null if there are no values
	 */
	public List getValuesList()
	{
		return values;
	}

	/**
	 * Dump state, suitable for debugging.
	 *
	 * @return Stringified form of this object
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer().append("[ option: ");

		buf.append(opt);

		if (longOpt != null)
		{
			buf.append(" ").append(longOpt);
		}

		buf.append(" ");

		if (hasArgs())
		{
			buf.append("[ARG...]");
		}
		else if (hasArg())
		{
			buf.append(" [ARG]");
		}

		buf.append(" :: ").append(description);

		if (type != null)
		{
			buf.append(" :: ").append(type);
		}

		buf.append(" ]");

		return buf.toString();
	}

	/**
	 * Returns whether this Option has any values.
	 *
	 * @return whether this Option has any values.
	 */
	private boolean hasNoValues()
	{
		return values.isEmpty();
	}

	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		Option option = (Option) o;


		if (opt != null ? !opt.equals(option.opt) : option.opt != null)
		{
			return false;
		}
		if (longOpt != null ? !longOpt.equals(option.longOpt) : option.longOpt != null)
		{
			return false;
		}

		return true;
	}

	public int hashCode()
	{
		int result;
		result = (opt != null ? opt.hashCode() : 0);
		result = 31 * result + (longOpt != null ? longOpt.hashCode() : 0);
		return result;
	}

	/**
	 * A rather odd clone method - due to incorrect code in 1.0 it is public
	 * and in 1.1 rather than throwing a CloneNotSupportedException it throws
	 * a RuntimeException so as to maintain backwards compat at the API level.
	 *
	 * After calling this method, it is very likely you will want to call
	 * clearValues().
	 *
	 * @throws RuntimeException
	 */
	public Object clone()
	{
		try
		{
			Option option = (Option) super.clone();
			option.values = new ArrayList(values);
			return option;
		}
		catch (CloneNotSupportedException cnse)
		{
			throw new RuntimeException("A CloneNotSupportedException was thrown: " + cnse.getMessage());
		}
	}

	/**
	 * Clear the Option values. After a parse is complete, these are left with
	 * data in them and they need clearing if another parse is done.
	 *
	 * See: <a href="https://issues.apache.org/jira/browse/CLI-71">CLI-71</a>
	 */
	public void clearValues()
	{
		values.clear();
	}

	/**
	 * This method is not intended to be used. It was a piece of internal
	 * API that was made public in 1.0. It currently throws an UnsupportedOperationException.
	 * @deprecated
	 * @throws UnsupportedOperationException
	 */
	public boolean addValue(String value)
	{
		throw new UnsupportedOperationException("The addValue method is not intended for client use. "
				+ "Subclasses should use the addValueForProcessing method instead. ");
	}

}
