package figures.listeners.creation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLabel;

import figures.Drawing;
import figures.Figure;
import figures.Star;
import history.HistoryManager;

public class StarCreationListener extends AbstractCreationListener {

	public StarCreationListener(Drawing model,HistoryManager<Figure> history, JLabel tipLabel)
	{
		super(model, history, tipLabel, 4);
		tips[0] = new String("Cliquez sur le bouton gauche, et tirez pour commencer votre star");
		tips[1] = new String("Relachez pour terminer");
		tips[2] = new String("Faire bouger la souris vers le haut/bas pour augmenter/diminuer les angles, puis Cliquez sur le bouton gauche pour terminer l'ajout des angles");
		tips[3] = new String("Cliquez sur le bouton gauche/droite pour agrandir le fond de la star, puis cliquez sur le bouton gauche pour terminer");
		
		updateTip();
	}

	/**
	 * Création d'une nouvelle figure rectangulaire de taille 0 au point de
	 * l'évènement souris, si le bouton appuyé est le bouton gauche.
	 *
	 * @param e l'évènement souris
	 * @see AbstractCreationListener#startFigure(MouseEvent)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 0))
		{
			startAction(e);
		}
	}

	/**
	 * Terminaison de la nouvelle figure rectangulaire si le bouton appuyé
	 * était le bouton gauche
	 * @param e l'évènement souris
	 * @see AbstractCreationListener#endFigure(MouseEvent)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if ((e.getButton() == MouseEvent.BUTTON1) && (currentStep == 1))
		{
			nextStep();
		}
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if ( (e.getButton() == MouseEvent.BUTTON1) && currentStep == 2)
		{
			Star star = (Star) currentFigure;
			star.setLastPoint(e.getPoint());
			nextStep();
			drawingModel.update();
		}
		else{
			if ( (e.getButton() == MouseEvent.BUTTON1) && currentStep == 3)
			{
				endFigure(e);
				drawingModel.update();
			}
		}
	}

	public void endFigure(MouseEvent e)
	{
		// Remise à zéro de currentStep pour pouvoir réutiliser ce
		// listener sur une autre figure
		nextStep();

		endPoint = e.getPoint();

		checkZeroSizeFigure();

		drawingModel.update();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Rien
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (currentStep == 2)
		{
			Star star = (Star) currentFigure;
			star.editNB(e.getPoint());
			drawingModel.update();
		}else 
			if (currentStep == 3)
			{
			Star star = (Star) currentFigure;
			double r = Math.abs(star.getInitialPoint().getX() - e.getPoint().getX());
			star.setInnerRadius(r/4);
			star.makestar(star.getInitialPoint(), star.getRayon());
			drawingModel.update();
		}
	}

	/**
	 * Déplacement du point en bas à droite de la figure rectangulaire, si
	 * l'on se trouve à l'étape 1 (après initalisation de la figure) et que
	 * le bouton enfoncé est bien le bouton gauche.
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (currentStep == 1)
		{
			Star star = (Star) currentFigure;
			double r = Math.abs(star.getInitialPoint().getX() - e.getPoint().getX());
			star.setInnerRadius(r/2);
			star.setStep2Y(e.getPoint().y);
			currentFigure.setLastPoint(e.getPoint());
			drawingModel.update();
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
