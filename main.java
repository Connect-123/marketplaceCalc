import javax.swing.*;
import java.awt.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.StringTokenizer;

public class main {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ShopGUI window = new ShopGUI();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}

class ShopGUI {

	JFrame frame;
	private JTextField productNameField, productPriceField, totalField, totalFieldInt, cashTotalField,
			cashTotalFieldInt, modifyProductPriceField, cashTotalFieldNoTax, totalFieldNoTax; // added
	private JComboBox<String> productBox, modifyProductNameBox, removeProductBox;
	private DefaultListModel<String> listModel;
	private Map<String, Double> products;
	private double total, cashTotal;
	private final double TAX_RATE = 14.975 / 100;
	private JList<String> basketList;

	public ShopGUI() {
		initialize();
		loadProductsFromFile();
		loadCashTotalFromFile();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		products = new HashMap<>();
		listModel = new DefaultListModel<>();

		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		tabbedPane.addTab("Checkout", null, panel1, "Manage Checkout");

		productBox = new JComboBox<>();
		productBox.setBounds(10, 11, 600, 30);
		panel1.add(productBox);

		JButton btnAddToBasket = new JButton("Add To Basket");
		btnAddToBasket.setBounds(620, 10, 150, 30);
		btnAddToBasket.addActionListener(e -> addToBasket());
		panel1.add(btnAddToBasket);

		basketList = new JList<>(listModel);
		basketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		basketList.setBounds(10, 51, 760, 400);
		panel1.add(basketList);

		JButton btnRemoveFromBasket = new JButton("Remove Item");
		btnRemoveFromBasket.setBounds(10, 460, 150, 30);
		btnRemoveFromBasket.addActionListener(e -> removeFromBasket());
		panel1.add(btnRemoveFromBasket);

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(200, 462, 46, 30);
		panel1.add(lblTotal);

		totalField = new JTextField();
		totalField.setEditable(false);
		totalField.setBounds(256, 462, 86, 30);
		panel1.add(totalField);
		totalField.setColumns(10);

		totalFieldInt = new JTextField();
		totalFieldInt.setEditable(false);
		totalFieldInt.setBounds(256, 502, 86, 30);
		panel1.add(totalFieldInt);
		totalFieldInt.setColumns(10);

		// added
		JLabel lblTotalNoTax = new JLabel("Total (No Tax):");
		lblTotalNoTax.setBounds(200, 542, 100, 30);
		panel1.add(lblTotalNoTax);

		totalFieldNoTax = new JTextField();
		totalFieldNoTax.setEditable(false);
		totalFieldNoTax.setBounds(256, 582, 86, 30);
		panel1.add(totalFieldNoTax);
		totalFieldNoTax.setColumns(10);

		JButton btnCash = new JButton("CASH");
		btnCash.setBounds(352, 462, 89, 30);
		btnCash.addActionListener(e -> cash());
		panel1.add(btnCash);

		JButton btnDebitCredit = new JButton("DEBIT/CREDIT");
		btnDebitCredit.setBounds(451, 462, 120, 30);
		btnDebitCredit.addActionListener(e -> debitCredit());
		panel1.add(btnDebitCredit);

		JLabel lblCashTotal = new JLabel("Cash Total:");
		lblCashTotal.setBounds(581, 462, 70, 30);
		panel1.add(lblCashTotal);

		cashTotalField = new JTextField();
		cashTotalField.setEditable(false);
		cashTotalField.setBounds(661, 462, 86, 30);
		panel1.add(cashTotalField);
		cashTotalField.setColumns(10);

		cashTotalFieldInt = new JTextField();
		cashTotalFieldInt.setEditable(false);
		cashTotalFieldInt.setBounds(661, 502, 86, 30);
		panel1.add(cashTotalFieldInt);
		cashTotalFieldInt.setColumns(10);

		// added
		JLabel lblCashTotalNoTax = new JLabel("Cash Total (No Tax):");
		lblCashTotalNoTax.setBounds(581, 542, 120, 30);
		panel1.add(lblCashTotalNoTax);

		cashTotalFieldNoTax = new JTextField();
		cashTotalFieldNoTax.setEditable(false);
		cashTotalFieldNoTax.setBounds(711, 542, 86, 30);
		panel1.add(cashTotalFieldNoTax);
		cashTotalFieldNoTax.setColumns(10);

		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(757, 462, 89, 30);
		btnReset.addActionListener(e -> reset());
		panel1.add(btnReset);

		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		tabbedPane.addTab("Product Management", null, panel2, "Manage Products");

		JLabel lblProductName = new JLabel("Add Product Name:");
		lblProductName.setBounds(10, 11, 130, 30);
		panel2.add(lblProductName);

		productNameField = new JTextField();
		productNameField.setBounds(150, 11, 150, 30);
		panel2.add(productNameField);
		productNameField.setColumns(10);

		JLabel lblProductPrice = new JLabel("Add Product Price:");
		lblProductPrice.setBounds(310, 11, 130, 30);
		panel2.add(lblProductPrice);

		productPriceField = new JTextField();
		productPriceField.setBounds(450, 11, 150, 30);
		panel2.add(productPriceField);
		productPriceField.setColumns(10);

		JButton btnAddProduct = new JButton("Add");
		btnAddProduct.setBounds(610, 11, 100, 30);
		btnAddProduct.addActionListener(e -> addProduct());
		panel2.add(btnAddProduct);

		JLabel lblRemoveProductName = new JLabel("Remove Product Name:");
		lblRemoveProductName.setBounds(10, 52, 150, 30);
		panel2.add(lblRemoveProductName);

		removeProductBox = new JComboBox<>();
		removeProductBox.setBounds(170, 52, 150, 30);
		panel2.add(removeProductBox);

		JButton btnRemoveProduct = new JButton("Remove");
		btnRemoveProduct.setBounds(330, 52, 100, 30);
		btnRemoveProduct.addActionListener(e -> removeProduct());
		panel2.add(btnRemoveProduct);

		JLabel lblModifyProductName = new JLabel("Modify Product Name:");
		lblModifyProductName.setBounds(10, 93, 150, 30);
		panel2.add(lblModifyProductName);

		modifyProductNameBox = new JComboBox<>();
		modifyProductNameBox.setBounds(170, 93, 150, 30);
		panel2.add(modifyProductNameBox);

		JLabel lblModifyProductPrice = new JLabel("Modify Product Price:");
		lblModifyProductPrice.setBounds(330, 93, 150, 30);
		panel2.add(lblModifyProductPrice);

		modifyProductPriceField = new JTextField();
		modifyProductPriceField.setBounds(490, 93, 150, 30);
		panel2.add(modifyProductPriceField);
		modifyProductPriceField.setColumns(10);

		JButton btnModifyProduct = new JButton("Modify");
		btnModifyProduct.setBounds(650, 93, 100, 30);
		btnModifyProduct.addActionListener(e -> modifyProduct());
		panel2.add(btnModifyProduct);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		AutoCompleteDecorator.decorate(productBox);
		AutoCompleteDecorator.decorate(modifyProductNameBox);
		AutoCompleteDecorator.decorate(removeProductBox);
	}

	private void addProduct() {
		String name = productNameField.getText();
		double price = Double.parseDouble(productPriceField.getText());
		products.put(name, price);
		productBox.addItem(name);
		modifyProductNameBox.addItem(name);
		removeProductBox.addItem(name);
		productNameField.setText("");
		productPriceField.setText("");
		saveProductsToFile();
	}

	private void removeProduct() {
		String name = (String) removeProductBox.getSelectedItem();
		products.remove(name);
		productBox.removeItem(name);
		modifyProductNameBox.removeItem(name);
		removeProductBox.removeItem(name);
		saveProductsToFile();
	}

	private void modifyProduct() {
		String name = (String) modifyProductNameBox.getSelectedItem();
		double price = Double.parseDouble(modifyProductPriceField.getText());
		products.put(name, price);
		modifyProductPriceField.setText("");
		saveProductsToFile();
	}

	private void addToBasket() {
		String name = (String) productBox.getSelectedItem();
		double price = products.get(name);
		total += price;
		listModel.addElement(name + " - " + price);
		totalField.setText(String.format("%.3f", total + total * TAX_RATE));
		totalFieldInt.setText(String.format("%.2f", total + total * TAX_RATE));
		totalFieldNoTax.setText(String.format("%.2f", total)); // added
	}

	private void removeFromBasket() {
		int selectedIndex = basketList.getSelectedIndex();
		if (selectedIndex != -1) {
			String item = listModel.getElementAt(selectedIndex);
			String name = item.split(" - ")[0];
			double price = products.get(name);
			total -= price;
			listModel.remove(selectedIndex);
			totalField.setText(String.format("%.3f", total + total * TAX_RATE));
			totalFieldInt.setText(String.format("%.2f", total + total * TAX_RATE));
			totalFieldNoTax.setText(String.format("%.2f", total)); // added
		}
	}

	private void cash() {
		cashTotal += total;
		cashTotalField.setText(String.format("%.3f", cashTotal + cashTotal * TAX_RATE));
		cashTotalFieldInt.setText(String.format("%.2f", cashTotal + cashTotal * TAX_RATE));
		cashTotalFieldNoTax.setText(String.format("%.2f", cashTotal)); // added
		total = 0;
		totalField.setText(String.format("%.3f", total));
		totalFieldInt.setText(String.format("%.2f", total));
		totalFieldNoTax.setText(String.format("%.2f", 0.0)); // added
		listModel.clear();
		saveCashTotalToFile();
	}

	private void debitCredit() {
		total = 0;
		totalField.setText(String.format("%.3f", total));
		totalFieldInt.setText(String.format("%.2f", total));
		totalFieldNoTax.setText(String.format("%.2f", 0.0)); // added
		listModel.clear();
	}

	private void reset() {
		total = 0;
		cashTotal = 0;
		totalField.setText(String.format("%.3f", total));
		totalFieldInt.setText(String.format("%.2f", total));
		cashTotalField.setText(String.format("%.3f", cashTotal));
		cashTotalFieldInt.setText(String.format("%.2f", cashTotal));
		cashTotalFieldNoTax.setText(String.format("%.2f", cashTotal)); // added
		totalFieldNoTax.setText(String.format("%.2f", 0.0)); // added
		listModel.clear();
		saveCashTotalToFile();
	}

	private void saveProductsToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("products.txt"))) {
			for (Map.Entry<String, Double> entry : products.entrySet()) {
				writer.println(entry.getKey() + "," + entry.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadProductsFromFile() {
		File file = new File("products.txt");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = reader.readLine()) != null) {
					StringTokenizer tokenizer = new StringTokenizer(line, ",");
					String name = tokenizer.nextToken();
					double price = Double.parseDouble(tokenizer.nextToken());
					products.put(name, price);
					productBox.addItem(name);
					modifyProductNameBox.addItem(name);
					removeProductBox.addItem(name);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveCashTotalToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter("cashtotal.txt"))) {
			writer.println(cashTotal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadCashTotalFromFile() {
		File file = new File("cashtotal.txt");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				cashTotal = Double.parseDouble(reader.readLine());
				cashTotalField.setText(String.format("%.3f", cashTotal + cashTotal * TAX_RATE));
				cashTotalFieldInt.setText(String.format("%.2f", cashTotal + cashTotal * TAX_RATE));
				cashTotalFieldNoTax.setText(String.format("%.2f", cashTotal)); // added
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
