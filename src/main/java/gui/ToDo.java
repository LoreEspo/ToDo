package gui;

import controller.Controller;
import logger.ToDoLogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


/**
 * Gui per i promemoria.
 */
public class ToDo {
    private static final Color LABEL_NORMAL = Color.BLACK;
    private static final Color LABEL_HOVERED = Color.BLACK.brighter().brighter();
    private static final Color LATE_NORMAL = Color.RED;
    private static final Color LATE_HOVERED = Color.RED.brighter().brighter();
    private static final Color COMPLETED_NORMAL = Color.GREEN;
    private static final Color COMPLETED_HOVERED = Color.GREEN.brighter().brighter();
    private static final Color URI_NORMAL = Color.BLUE;
    private static final Color URI_HOVERED = Color.BLUE.brighter().brighter();

    private JPanel panel;
    private JLabel titolo;
    private JTextArea descrizione;
    private JCheckBox stato;
    private JPanel containerImmagine;
    private JLabel labelImmagine;
    private JButton immagineButton;
    private JPanel containerLink;
    private JLabel link;
    private JButton linkButton;
    private JPanel containerAttivita;
    private JButton attivitaButton;
    private JButton dataButton;
    private JButton colorButton;
    private JButton cancellaButton;
    private JButton condividiButton;
    private JButton spostaSinistraButton;
    private JButton spostaDestraButton;
    private JButton spostaBachecaButton;

    private final ArrayList<Attivita> listaAttivita = new ArrayList<>();
    private final Controller controller;
    private final GregorianCalendar calendario = (GregorianCalendar) Calendar.getInstance();
    private Date scadenza;
    private int id = -1;
    private boolean condiviso = false;

    /**
     * Instanzia un nuovo promemoria.
     * Se il promemoria è condiviso, la gui non sarà interagibile
     * e sarà read-only.
     *
     * @param frame     il frame che lo contiene
     * @param id        l'id del promemoria al quale è legata la gui
     * @param condiviso se il promemoria è condiviso
     */
    public ToDo(JFrame frame, int id, boolean condiviso) {
        this.controller = Controller.getInstance();
        this.id = id;
        this.condiviso = condiviso;

        creaGUI();

        creaListener(frame, id);

        for (int i : controller.attivitaToDo(id)) {
            aggiungiAttivita(i);
        }

        setColoreTitolo();
    }

    private void creaListener(JFrame frame, int id) {
        // Apri il link
        link.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (link.getText().isEmpty()) {
                            return;
                        }
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(new URI(link.getText()));
                            } catch (URISyntaxException _) {
                                JOptionPane.showMessageDialog(
                                        panel, "URI non valido.",
                                        "URI error", JOptionPane.ERROR_MESSAGE
                                );
                            } catch (IOException _) {
                                JOptionPane.showMessageDialog(
                                        panel, "Non è stato possibile aprire l'URI.",
                                        "Browser error", JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Non necessario
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // Non necessario
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        link.setForeground(URI_HOVERED);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        link.setForeground(URI_NORMAL);
                    }
                }
        );

        if (condiviso) {
            return;
        }

        // Modifica il titolo quando premuto
        titolo.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cambiaTitolo();
                    }


                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Non fare niente
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // Non fare niente
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        setColoreTitolo(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        setColoreTitolo(false);
                    }
                }
        );

        descrizione.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        controller.setDescrizioneToDo(id, descrizione.getText());
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        controller.setDescrizioneToDo(id, descrizione.getText());
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        // Non necessario poiché non chiamato per le JTextArea
                    }
                }
        );

        stato.addActionListener(_ -> {
            controller.setCompletatoToDo(id, stato.isSelected());
            setColoreTitolo();
        });

        // Cambia immagine con il tasto sinistro e rimuovila con il tasto destro
        labelImmagine.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            cambiaImmagine();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            int opzione = JOptionPane.showConfirmDialog(
                                    frame, "Rimuovere l'immagine?",
                                    "Immagine", JOptionPane.OK_CANCEL_OPTION);
                            if (opzione == 0) {
                                rimuoviImmagine();
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Non necessario
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        // Non necessario
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Non necessario
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Non necessario
                    }
                }
        );

        linkButton.addActionListener(_ -> {
            String url = JOptionPane.showInputDialog(
                    panel, "Imposta l'url dell'attività (Vuoto per rimuovere).",
                    "Link attività", JOptionPane.QUESTION_MESSAGE
            );
            if (url != null) {
                controller.setLinkToDo(id, url);
                link.setText(url);
            }
        });


        dataButton.addActionListener(_ -> {
            calendario.setTime(new Date());
            Date current = calendario.getTime();

            SelettoreData selettore = SelettoreData.create(scadenza != null ? scadenza : current);
            int result = selettore.getRisposta();

            if (result == SelettoreData.RISPOSTA_CANCEL) {
                return;
            }
            scadenza = selettore.getData();
            controller.setScadenzaToDo(id, scadenza);
            aggiornaScadenza();
            setColoreTitolo();
        });

        colorButton.addActionListener(_ -> cambiaColore());

        condividiButton.addActionListener(_ -> MenuCondivisione.create(id));

    }

    private void cambiaTitolo() {
        String nuovoTitolo = JOptionPane.showInputDialog("Scegli il titolo");
        if (nuovoTitolo != null && !nuovoTitolo.isEmpty() && !nuovoTitolo.equals(titolo.getText())) {
            controller.setTitoloToDo(id, nuovoTitolo);
            titolo.setText(nuovoTitolo);
        }
    }

    private void aggiornaScadenza() {
        if (scadenza == null) {
            dataButton.setText("No scadenza");
            return;
        }
        calendario.setTime(scadenza);
        dataButton.setText(
                String.format(
                        "%d/%d/%d",
                        calendario.get(Calendar.DAY_OF_MONTH),
                        calendario.get(Calendar.MONTH) + 1,
                        calendario.get(Calendar.YEAR)
                )
        );
    }

    /// Crea la gui da zero e ordinala con i layout adatti.
    /// Read-only se condiviso.
    private void creaGUI() {
        panel = new JPanel();
        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );
        panel.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED)
        );

        titolo = new JLabel();
        panel.add(titolo);
        titolo.setText(controller.getTitoloToDo(id));
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titolo.setFont(titolo.getFont().deriveFont(18.0f));

        if (condiviso) {
            JLabel autore = new JLabel(controller.getAutoreToDo(id));
            panel.add(autore);
            panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }


        JPanel tmpPanel = new JPanel();
        tmpPanel.setLayout(new BorderLayout());
        descrizione = new JTextArea(controller.getDescrizioneToDo(id));
        tmpPanel.add(descrizione, BorderLayout.CENTER);
        descrizione.setBackground(new Color(240, 240, 240));
        descrizione.setLineWrap(true);
        descrizione.setWrapStyleWord(true);
        descrizione.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        );
        descrizione.setEnabled(!condiviso);

        stato = new JCheckBox();
        stato.setSelected(controller.getCompletatoToDo(id));
        tmpPanel.add(stato, BorderLayout.EAST);
        panel.add(tmpPanel);
        tmpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tmpPanel.setMaximumSize(
                new Dimension(9999, tmpPanel.getPreferredSize().height)
        );
        stato.setEnabled(!condiviso);

        containerImmagine = new JPanel();
        containerImmagine.setLayout(
                new BorderLayout()
        );
        labelImmagine = new JLabel();
        labelImmagine.setVisible(false);
        immagineButton = new JButton("Seleziona immagine");
        immagineButton.addActionListener(_ -> cambiaImmagine());
        containerImmagine.add(labelImmagine, BorderLayout.CENTER);
        containerImmagine.add(immagineButton, BorderLayout.SOUTH);
        containerImmagine.setMaximumSize(
                new Dimension(9999, containerImmagine.getPreferredSize().height)
        );
        panel.add(containerImmagine);
        immagineButton.setMaximumSize(
                new Dimension(9999, immagineButton.getPreferredSize().height)
        );
        immagineButton.setEnabled(!condiviso);
        immagineButton.setVisible(!condiviso);
        // Aspetta mezzo secondo per fare in modo che il pannello
        // abbia impostate le dimensioni
        Timer timer = new Timer(
                250, e -> setImmagine()
        );
        timer.setRepeats(false);
        timer.start();

        containerLink = new JPanel();
        containerLink.setLayout(
                new BorderLayout()
        );
        link = new JLabel(controller.getLinkToDo(id));
        link.setHorizontalAlignment(SwingConstants.CENTER);
        link.setForeground(URI_NORMAL);
        if (!condiviso) {
            linkButton = new JButton("\uD83D\uDD89");
            containerLink.add(link, BorderLayout.CENTER);
            containerLink.add(linkButton, BorderLayout.EAST);
            panel.add(containerLink);
        }


        tmpPanel = new JPanel(new GridLayout(1, condiviso ? 1 : 2));
        if (!condiviso) {
            colorButton = new JButton();
            tmpPanel.add(colorButton);
            colorButton.setText("Sfondo");
        }

        dataButton = new JButton();
        tmpPanel.add(dataButton);
        dataButton.setText("No scadenza");
        panel.add(tmpPanel);
        tmpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tmpPanel.setMaximumSize(
                new Dimension(9999, tmpPanel.getPreferredSize().height)
        );
        scadenza = controller.getScadenzaToDo(id);
        aggiornaScadenza();
        dataButton.setEnabled(!condiviso);

        containerAttivita = new JPanel();
        containerAttivita.setLayout(
                new GridLayout(0, 1)
        );
        panel.add(containerAttivita);
        if (!condiviso) {
            attivitaButton = new JButton("Aggiungi attività");
            attivitaButton.addActionListener(_ -> aggiungiAttivita());
            containerAttivita.add(attivitaButton);
        }

        if (!condiviso) {
            tmpPanel = new JPanel();
            tmpPanel.setLayout(new GridLayout(1, 2));
            condividiButton = new JButton();
            condividiButton.setText("Condividi");
            cancellaButton = new JButton();
            cancellaButton.setText("Cancella");
            tmpPanel.add(condividiButton);
            tmpPanel.add(cancellaButton);
            panel.add(tmpPanel);
            tmpPanel.setMaximumSize(
                    new Dimension(9999, tmpPanel.getPreferredSize().height)
            );
        }

        if (!condiviso) {
            tmpPanel = new JPanel(new BorderLayout());
            spostaBachecaButton = new JButton("Cambia bacheca");
            tmpPanel.add(spostaBachecaButton, BorderLayout.CENTER);
            panel.add(tmpPanel);

            tmpPanel = new JPanel();
            tmpPanel.setLayout(new GridLayout(1, 2));
            spostaSinistraButton = new JButton();
            spostaSinistraButton.setText("⟵");
            tmpPanel.add(spostaSinistraButton);

            spostaDestraButton = new JButton();
            spostaDestraButton.setText("⟶");
            tmpPanel.add(spostaDestraButton);
            panel.add(tmpPanel);
            tmpPanel.setMaximumSize(
                    new Dimension(9999, tmpPanel.getPreferredSize().height)
            );
        }


        panel.setMaximumSize(
                new Dimension(9999, panel.getPreferredSize().height)
        );

        String colore = controller.getColoreToDo(id);
        if (colore != null) {
            impostaColore(new Color(Integer.parseInt(colore, 16)));
        } else {
            impostaColore(panel.getBackground());
        }

        panel.setVisible(true);
        panel.repaint();
        panel.revalidate();
    }

    /**
     * @return il JPanel della gui
     */
    public JPanel getPanel() { return panel; }

    /**
     * @return l'id legato alla gui
     */
    public int getId() { return id; }

    /**
     * @return il tasto per cancellare il promemoria
     */
    public JButton getCancellaButton() { return cancellaButton; }

    /**
     * @return il tasto per spostare il promemoria a sinistra
     */
    public JButton getSpostaSinistraButton() { return spostaSinistraButton; }

    /**
     * @return il tasto per spostare il promemoria a destra
     */
    public JButton getSpostaDestraButton() { return spostaDestraButton; }

    /**
     * @return il tasto per spostare il promemoria in un'altra bacheca
     */
    public JButton getSpostaBachecaButton() { return spostaBachecaButton; }


    private boolean inRitardo() {
        if (scadenza == null)
            return false;
        calendario.set(Calendar.HOUR, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);

        Date current = calendario.getTime();
        return current.compareTo(scadenza) > 0;
    }

    /// Apri l'esploratore e seleziona l'immagine da
    /// aprire come byte array.
    private void cambiaImmagine() {
        JFileChooser fc = getSelettoreImmagini();
        int returnValue = fc.showOpenDialog(panel);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            byte[] bytes;

            try {
                bytes = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                ToDoLogger.getInstance().logError(e);
                JOptionPane.showMessageDialog(
                        panel, "Errore nell'apertura dell'immagine.",
                        "Image error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            setImmagine(bytes);
        }
    }

    ///  Crea un filtro per le immagini e aggiungilo all'esploratore file.
    private static JFileChooser getSelettoreImmagini() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String ext = getExtension(f.getName());
                if (ext == null) {
                    return false;
                }
                return ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg");
            }

            @Override
            public String getDescription() {
                return "Immagini";
            }

            private String getExtension(String s) {
                String ext = null;
                int i = s.lastIndexOf('.');

                if (i > 0 &&  i < s.length() - 1) {
                    ext = s.substring(i+1).toLowerCase();
                }
                return ext;
            }
        };

        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(filter);
        return fc;
    }

    private void setImmagine() {
        byte[] dati = controller.getImmagineToDo(id);
        if (dati == null) {
            return;
        }
        setImmagine(dati, false);
    }

    private void setImmagine(byte[] dati) {
        setImmagine(dati, true);
    }

    /// Imposta l'immagine, ridimensionandola alle dimensioni della gui.
    private void setImmagine(byte[] dati, boolean aggiorna) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(dati);
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore nell'apertura dell'immagine.",
                    "Image error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        controller.setImmagineToDo(id, dati, aggiorna);
        int width = panel.getWidth();
        float scale = (float) width / (float) image.getWidth();
        int height = (int)(image.getHeight() * scale);
        ImageIcon icona = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_FAST));
        labelImmagine.setIcon(icona);
        labelImmagine.setVisible(true);
        immagineButton.setVisible(false);
    }

    private void rimuoviImmagine() {
        labelImmagine.setIcon(null);
        controller.setImmagineToDo(id, null);
        labelImmagine.setVisible(false);
        if (condiviso)
            immagineButton.setVisible(true);
    }

    /// Aggiungi una nuova attività.
    private void aggiungiAttivita() {
        int idAttivita;

        try {
            idAttivita = controller.aggiungiAttivita(id);
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore durante l'aggiunta dell'attività.",
                    "Activity error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        aggiungiAttivita(idAttivita);
    }

    /// Aggiunge la gui di un'attività legata all'id dato in input.
    /// Se condiviso, disabilità l'attività.
    private void aggiungiAttivita(int idAttivita) {
        Attivita attivita = new Attivita(idAttivita);

        containerAttivita.add(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        listaAttivita.add(attivita);
        attivita.setColore(panel.getBackground());


        if (!condiviso) {
            attivita.getCancellaButton().addActionListener(
                    _ -> rimuoviAttivita(attivita)
            );
            attivita.getCheckbox().addActionListener(_ -> setColoreTitolo());
        } else {
            attivita.disabilita();
        }

        stato.setVisible(false);
        setColoreTitolo();
    }

    private void rimuoviAttivita(Attivita attivita) {
        try {
            controller.rimuoviAttivita(this.id, attivita.getId());
        } catch (SQLException e) {
            ToDoLogger.getInstance().logError(e);
            JOptionPane.showMessageDialog(
                    panel, "Errore durante la rimozione dell'attività.",
                    "Activity error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        listaAttivita.remove(attivita);
        containerAttivita.remove(attivita.getPanel());
        containerAttivita.repaint();
        containerAttivita.revalidate();
        if (listaAttivita.isEmpty())
            stato.setVisible(true);
    }

    /// Cambia il colore di sfondo
    private void cambiaColore() {
        SelettoreColore selettore = SelettoreColore.create(panel.getBackground());
        if (!selettore.isOk()) {
            return;
        }
        Color colore = selettore.getColore();
        controller.setColoreToDo(
                id, String.format("%02X%02X%02X", colore.getRed(), colore.getGreen(), colore.getBlue())
                );
        impostaColore(colore);
    }

    /// Cambia il colore del titolo a seconda dello stato del promemoria
    private void setColoreTitolo(boolean hovered) {
        if (hovered) {
            if (controller.getCompletatoToDo(id)) {
                titolo.setForeground(COMPLETED_HOVERED);
                return;
            }
            titolo.setForeground(inRitardo() ? LATE_HOVERED : LABEL_HOVERED);
        } else {
            if (controller.getCompletatoToDo(id)) {
                titolo.setForeground(COMPLETED_NORMAL);
                return;
            }
            titolo.setForeground(inRitardo() ? LATE_NORMAL : LABEL_NORMAL);
        }
    }

    private void setColoreTitolo() {
        setColoreTitolo(false);
    }

    private void impostaColore(Color colore) {
        // Itera su tutti i componenti al quale va cambiato il colore
        JComponent[] componentiColorati = {
                panel, descrizione, stato, containerImmagine,
                immagineButton, containerLink, link, linkButton,
                containerAttivita, attivitaButton, dataButton,
                colorButton, cancellaButton, condividiButton,
                spostaSinistraButton, spostaDestraButton, spostaBachecaButton
        };

        labelImmagine.setBackground(new Color(0, 0, 0, 0));

        for (JComponent component : componentiColorati) {
            if (component != null) {
                component.setBackground(colore);
            }
        }

        // Cambia colore anche alle attività
        for (Attivita attivita : listaAttivita) {
            attivita.setColore(panel.getBackground());
        }
    }
}
