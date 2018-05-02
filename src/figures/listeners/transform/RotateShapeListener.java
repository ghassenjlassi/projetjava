package figures.listeners.transform;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.listeners.AbstractFigureListener;
import history.HistoryManager;

/**
 * Listener permettant de déplacer une figure
 * <ol>
 * 	<li>bouton 1 pressé et maintenu enfoncé</li>
 * 	<li>déplacement de la souris avec le bouton enfoncé</li>
 * 	<li>relachement du bouton</li>
 * </ol>
 * @author davidroussel
 */
public class RotateShapeListener extends AbstractTransformShapeListener
{
	/**
	 * Le dernier point
	 * @note Utilisé pour calculer le déplacement entre l'évènement courant
	 * et l'évènement précédent.
	 * @note modifié dans {@link #mouseDragged(MouseEvent)}
	 */
	private Point2D lastPoint;
	private Rectangle2D bounds;
	

	/**
	 * Constructeur d'un listener à deux étapes: pressed->drag->release pour
	 * déplacer toutes les figures
	 * @param model le modèle de dessin à modifier par ce Listener
	 * @param tipLabel le label dans lequel afficher les conseils utilisateur
	 */
	public RotateShapeListener(Drawing model, HistoryManager<Figure> history, JLabel tipLabel) {
        super(model, history, tipLabel);
        // Auto-generated constructor stub
        keyMask = InputEvent.CTRL_MASK;
    }

	/* (non-Javadoc)
	 * @see figures.listeners.transform.AbstractTransformShapeListener#init()
	 */
	@Override
	   public void init() {
        // Auto-generated method stub
        lastPoint = startPoint;
        if (currentFigure != null)
        {
            center = currentFigure.getCenter();
            initialTransform = currentFigure.getRotation();
        }
        else
        {
            System.err.println(getClass().getSimpleName() + "::init : null figure");
        }

    }
	/* (non-Javadoc)
	 * @see figures.listeners.transform.AbstractTransformShapeListener#updateDrag(java.awt.event.MouseEvent)
	 */
	@Override
	public void updateDrag(MouseEvent e)
	{
		Point2D currentPoint = e.getPoint();
        double dy = currentPoint.getY() - lastPoint.getY();
        double theta = dy * Math.PI / 180;

        AffineTransform rotate =AffineTransform.getRotateInstance(theta);
        rotate.concatenate(initialTransform);
        currentFigure.setRotation(rotate);
	}
}
