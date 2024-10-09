import jess.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class SaludAppGUI {
    private Rete motor;

    public SaludAppGUI() {
        motor = new Rete();
        cargarReglasDesdeArchivo(); 

      
        JFrame frame = new JFrame("Asesor de Salud");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);

        JLabel edadLabel = new JLabel("Edad:");
        edadLabel.setBounds(10, 20, 80, 25);
        frame.add(edadLabel);

        JTextField edadField = new JTextField();
        edadField.setBounds(100, 20, 160, 25);
        frame.add(edadField);

        JLabel pesoLabel = new JLabel("Peso (kg):");
        pesoLabel.setBounds(10, 60, 80, 25);
        frame.add(pesoLabel);

        JTextField pesoField = new JTextField();
        pesoField.setBounds(100, 60, 160, 25);
        frame.add(pesoField);

        JLabel diabetesLabel = new JLabel("Diabetes:");
        diabetesLabel.setBounds(10, 100, 80, 25);
        frame.add(diabetesLabel);

        String[] diabetesOptions = {"si", "no"};
        JComboBox<String> diabetesCombo = new JComboBox<>(diabetesOptions);
        diabetesCombo.setBounds(100, 100, 160, 25);
        frame.add(diabetesCombo);

        JLabel saludFisicaLabel = new JLabel("Salud Física:");
        saludFisicaLabel.setBounds(10, 140, 80, 25);
        frame.add(saludFisicaLabel);

        String[] saludFisicaOptions = {"normal", "bajo-peso", "sobrepeso"};
        JComboBox<String> saludFisicaCombo = new JComboBox<>(saludFisicaOptions);
        saludFisicaCombo.setBounds(100, 140, 160, 25);
        frame.add(saludFisicaCombo);

        JLabel saludMentalLabel = new JLabel("Salud Mental:");
        saludMentalLabel.setBounds(10, 180, 80, 25);
        frame.add(saludMentalLabel);

        String[] saludMentalOptions = {"estable", "estres", "depresion"};
        JComboBox<String> saludMentalCombo = new JComboBox<>(saludMentalOptions);
        saludMentalCombo.setBounds(100, 180, 160, 25);
        frame.add(saludMentalCombo);

        JButton button = new JButton("Obtener Recomendación");
        button.setBounds(10, 220, 250, 25);
        frame.add(button);

        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setBounds(10, 260, 360, 100);
        resultadoArea.setEditable(false);
        frame.add(resultadoArea);

   
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int edad = Integer.parseInt(edadField.getText());
            int peso = Integer.parseInt(pesoField.getText());
            String diabetes = diabetesCombo.getSelectedItem().toString();
            String saludFisica = saludFisicaCombo.getSelectedItem().toString();
            String saludMental = saludMentalCombo.getSelectedItem().toString();

            // Insertar hecho en el motor de reglas
            Fact persona = new Fact("persona", motor);
            persona.setSlotValue("edad", new Value(edad, RU.INTEGER));
            persona.setSlotValue("peso", new Value(peso, RU.INTEGER));
            persona.setSlotValue("diabetes", new Value(diabetes, RU.STRING));
            persona.setSlotValue("salud-fisica", new Value(saludFisica, RU.STRING));
            persona.setSlotValue("salud-mental", new Value(saludMental, RU.STRING));
            motor.assertFact(persona);

     
            motor.run();

          
StringBuilder resultado = new StringBuilder();
Iterator hechos = motor.listFacts();
boolean hayRecomendaciones = false; 

while (hechos.hasNext()) {
    Fact hecho = (Fact) hechos.next();
    if (hecho.getName().equals("MAIN::recomendacion")) {
        // Obtener el texto de la recomendación
        String recomendacionTexto = hecho.getSlotValue("texto").stringValue(motor.getGlobalContext());
        resultado.append("Recomendación: ").append(recomendacionTexto).append("\n");
        hayRecomendaciones = true; // Hay al menos una recomendación
    }
}


if (hayRecomendaciones) {
    resultadoArea.setText(resultado.toString()); // Mostrar recomendaciones
} else {
    resultadoArea.setText("No se generó ninguna recomendación."); 
}


        } catch (Exception ex) {
            ex.printStackTrace();
            resultadoArea.setText("Error: " + ex.getMessage());
        }
    }
});



        frame.setVisible(true);
    }

    private void cargarReglasDesdeArchivo() {
        try {

            BufferedReader reader = new BufferedReader(new FileReader("reglas_salud.clp"));
            StringBuilder reglas = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                reglas.append(linea).append("\n");
            }
            reader.close();

           
            System.out.println("Cargando reglas...");
            motor.executeCommand(reglas.toString());
            System.out.println("Reglas cargadas correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SaludAppGUI();
    }
}
