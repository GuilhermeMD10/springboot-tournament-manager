package pt.ul.fc.di.css.soccernowjavafx.model;

import java.io.File;
import java.util.List;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {
    
	/*
	 * in this way personList also reports
	 * mutations of the elements in it by using the given extractor.
	 * Observable objects returned by extractor (applied to each list element) are
	 * listened
	 * for changes and transformed into "update" change of ListChangeListener.
	 * since the phone is not visible, changes in the phone do not need to be
	 * propagated
	 */

	private final ObservableList<Customer> personList = FXCollections
			.observableArrayList(person -> new Observable[] { person.getNomeProperty(), person.getPhoneProperty(),
					person.getVatProperty() });

	public ObservableList<Customer> getCustomerList() {
		return personList;
	}

	private final ObjectProperty<Customer> currentCustomer = new SimpleObjectProperty<>(null);

	public ObjectProperty<Customer> currentCustomerProperty() {
		return currentCustomer;
	}

	public final Customer getCurrentCustomer() {
		return currentCustomerProperty().get();
	}

	public final void setCurrentCustomer(Customer person) {
		currentCustomerProperty().set(person);
	}

	public void loadData(File file) {
		// mock...
		personList.setAll(
				new Customer(1, "Jose", "914029074", "934445678"),
				new Customer(2, "Isabel", "924079074", "912765432"),
				new Customer(3, "Eloi", "914069074", "965436576"),
				new Customer(4, "Ema", "911079374", "217122121"),
				new Customer(5, "Paulo", "957079074", "217500504"));
	}

	public void saveData(File file) {
	}

	public void setCustomerList(List<Customer> customerList) {
		personList.setAll(customerList);
	}
}
