package fr.irit.smac.may.serial;

public abstract class SerialPortComp {

	private Component structure = null;

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.serial.iface.SerialPortStatus statusPort();

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.serial.iface.SendPort sendingPort();

	public static interface Bridge {

	}

	public static final class Component {

		@SuppressWarnings("unused")
		private final Bridge bridge;

		private final SerialPortComp implementation;

		public Component(final SerialPortComp implem, final Bridge b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.statusPort = implem.statusPort();
			this.sendingPort = implem.sendingPort();

		}

		private final fr.irit.smac.may.serial.iface.SerialPortStatus statusPort;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.serial.iface.SerialPortStatus statusPort() {
			return this.statusPort;
		};
		private final fr.irit.smac.may.serial.iface.SendPort sendingPort;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.serial.iface.SendPort sendingPort() {
			return this.sendingPort;
		};

		public final void start() {

			this.implementation.start();
		}
	}

	/**
	 * Can be overridden by the implementation
	 * It will be called after the component has been instantiated, after the components have been instantiated
	 * and during the containing component start() method is called.
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected void start() {
	}

	public static final Component createComponent(SerialPortComp _compo) {
		return new Component(_compo, new Bridge() {
		});
	}

}
