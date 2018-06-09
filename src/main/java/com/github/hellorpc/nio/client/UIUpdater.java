package com.github.hellorpc.nio.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * <p>����̨�̵߳��ø���UI���࣬�ṩͨ�õ�UI���»��ƣ�Ҳ���ڽ���Ϻ�̨�����UI���ƺ�ʹ���¼����ƻ�õ�?
 * ���ն�UI�ĸ���Ҳ�Ƿŵ�EDT�߳���ִ�С�</p>
 * 
 * <p>ע������������߳�ͬʱ����ͬһ��UIUpdaterʵ����register��update�������������߳�ͬ�����⡣
 * ����WorkerTask��ReadMessageTask�����߳�ͬʱ����register������ͬʱ����uiMapʱ�ͻ�����ͬ�����⡣</p>
 * 
 * <p>��������û�������������uiMap�ķ������ն�����EDT�߳��У�Ҳ�Ͳ���Ҫ�����߳�ͬ�������ˡ�</p>
 * 
 * @author XUHAILIN730
 * 
 */
public class UIUpdater {

	private static Map<Class<? extends JComponent>, Item> COMPONENT_MAP = new ConcurrentHashMap<Class<? extends JComponent>, Item>();

	// TODO: ���������JComponent
	static {
		COMPONENT_MAP.put(JTextField.class, new Item("setText",
				new Class[] { String.class }));
		COMPONENT_MAP.put(JLabel.class, new Item("setText",
				new Class[] { String.class }));
		COMPONENT_MAP.put(JTextArea.class, new Item("setText",
				new Class[] { String.class }));
		COMPONENT_MAP.put(JButton.class, new Item("setText",
				new Class[] { String.class }));
	}

	private Map<String, JComponent> uiMap = new HashMap<String, JComponent>();

	/**
	 * ע����Ҫʹ��UIUpdater���и��µ�UI component
	 * 
	 * @param name
	 * @param component
	 */
	public void register(String name, JComponent component) {
		uiMap.put(name, component);
	}

	/**
	 * ����UI�ؼ���ֵ
	 * 
	 * @param name
	 * @param values
	 */
	public void update(String name, Object[] values) {
		final String fName = name;
		final Object[] fValue = values;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JComponent component = uiMap.get(fName);
				if (component == null) {
					return;
				}
				Item item = COMPONENT_MAP.get(component.getClass());

				try {
					Method m = component.getClass().getMethod(item.methodName,
							item.parameterTypes);
					m.invoke(component, fValue);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		});
	}

	static class Item {
		String methodName;
		Class<?>[] parameterTypes;

		public Item(String methodName, Class<?>[] parameterTypes) {
			super();
			this.methodName = methodName;
			this.parameterTypes = parameterTypes;
		}
	}
}
