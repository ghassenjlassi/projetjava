package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Stream;
import figures.enums.FigureType;
import figures.enums.LineType;

import history.Memento;
import history.Originator;
import utils.StrokeFactory;
import filters.EdgeColorFilter;
import filters.FigureFilters;
import filters.FillColorFilter;
import filters.LineFilter;
import filters.ShapeFilter;


/**
 * Classe contenant l'ensemble des figures à dessiner (LE MODELE)
 * @author davidroussel
 */
public class Drawing extends Observable implements Originator<Figure>
{
	/**
	 * Liste des figures à dessiner (protected pour que les classes du même
	 * package puissent y accéder)
	 */
	protected Vector<Figure> figures;

	/**
	 * Liste triée des indices (uniques) des figures sélectionnées
	 */
	protected SortedSet<Integer> selectionIndex;

	/**
	 * Figure située sous le curseur.
	 * Déterminé par {@link #getFigureAt(Point2D)}
	 */
	private Figure selectedFigure;

	/**
	 * Le type de figure à créer (pour la prochaine figure)
	 */
	private FigureType type;

	/**
	 * La couleur de remplissage courante (pour la prochaine figure)
	 */
	private Paint fillPaint;

	/**
	 * La couleur de trait courante (pour la prochaine figure)
	 */
	private Paint edgePaint;

	/**
	 * La largeur de trait courante (pour la prochaine figure)
	 */
	private float edgeWidth;

	/**
	 * Le type de trait courant (sans trait, trait plein, trait pointillé,
	 * pour la prochaine figure)
	 */
	private LineType edgeType;

	/**
	 * Les caractétistique à appliquer au trait en fonction de {@link #type} et
	 * {@link #edgeWidth}
	 */
	private BasicStroke stroke;

	/**
	 * Etat de filtrage des figures dans le flux de figures fournit par
	 * {@link #stream()}
	 * Lorsque {@link #filtering} est true le dessin des figures est filtré
	 * par l'ensemble des filtres présents dans {@link #shapeFilters},
	 * {@link #fillColorFilter}, {@link #edgeColorFilter} et
	 * {@link #lineFilters}.
	 * Lorsque {@link #filtering} est false, toutes les figures sont fournies
	 * dans le flux des figures.
	 * @see #stream()
	 */
	private boolean filtering;

	/**
	 * Filtres à appliquer au flux des figures pour sélectionner les types
	 * de figures à afficher
	 * @see #stream()
	 */
	private FigureFilters<FigureType> shapeFilters;

	
	/**
	 * Filtre à appliquer au flux des figures pour sélectionner les figures
	 * ayant une couleur particulière de remplissage
	 */
	 private FillColorFilter fillColorFilter; // TODO décommenter lorsque prêt

	/**
	 * Filtre à appliquer au flux des figures pour sélectionner les figures
	 * ayant une couleur particulière de trait
	 */
	 private EdgeColorFilter edgeColorFilter; // TODO décommenter lorsque prêt

	/**
	 * Filtres à applique au flux des figures pour sélectionner les figures
	 * ayant un type particulier de lignes
	 */
	private FigureFilters<LineType> lineFilters;

	/**
	 * Constructeur de modèle de dessin
	 */
	public Drawing()
	{
		figures = new Vector<Figure>();
		selectionIndex = new TreeSet<Integer>(Integer::compare);
		shapeFilters = new FigureFilters<FigureType>();

		 fillColorFilter = null; // TODO  décommenter lorsque prêt
		 edgeColorFilter = null; // TODO  décommenter lorsque prêt
		lineFilters = new FigureFilters<LineType>();

		fillPaint = null;
		edgePaint = null;
		edgeWidth = 1.0f;
		edgeType = LineType.SOLID;
		stroke = StrokeFactory.getStroke(edgeType, edgeWidth);
		filtering = false;
		selectedFigure = null;
		System.out.println("Drawing model created");
	}

	/**
	 * Nettoyage avant destruction
	 */
	@Override
	protected void finalize()
	{
		// Aide au GC
		figures.clear();
		figures = null;
		selectionIndex.clear();
		selectionIndex = null;
		fillPaint = null;
		edgePaint = null;
		edgeType = null;
		stroke = null;
		shapeFilters.clear();
		shapeFilters = null;
		  fillColorFilter = null; // TODO décommenter lorsque prêt
		  edgeColorFilter = null; // TODO décommenter lorsque prêt
		lineFilters.clear();
		lineFilters = null;
	}

	/**
	 * Mise à jour du ou des {@link Observer} qui observent ce modèle. On place
	 * le modèle dans un état "changé" puis on notifie les observateurs.
	 */
	public void update()
	{
		setChanged();
		notifyObservers(); // pour que les observateurs soient mis à jour
	}

	/**
	 * Mise en place d'un nouveau type de figure à générer
	 * @param type le nouveau type de figure
	 */
	public void setFigureType(FigureType type)
	{
		this.type = type;
	}

	/**
	 * Accesseur de la couleur de remplissage courante des figures
	 * @return la couleur de remplissage courante des figures
	 */
	public Paint getFillpaint()
	{
		return fillPaint;
	}

	/**
	 * Mise en place d'une nouvelle couleur de remplissage
	 * @param fillPaint la nouvelle couleur de remplissage
	 */
	public void setFillPaint(Paint fillPaint)
	{
		this.fillPaint = fillPaint;
	}

	/**
	 * Accesseur de la couleur de trait courante des figures
	 * @return la couleur de remplissage courante des figures
	 */
	public Paint getEdgePaint()
	{
		return edgePaint;
	}

	/**
	 * Mise en place d'une nouvelle couleur de trait
	 * @param edgePaint la nouvelle couleur de trait
	 */
	public void setEdgePaint(Paint edgePaint)
	{
		this.edgePaint = edgePaint;
	}

	/**
	 * Accesseur du trait courant des figures
	 * @return le trait courant des figures
	 */
	public BasicStroke getStroke()
	{
		return stroke;
	}

	/**
	 * Mise en place d'un nouvelle épaisseur de trait
	 * @param width la nouvelle épaisseur de trait
	 */
	public void setEdgeWidth(float width)
	{
		
		edgeWidth = width;
		/*
		 * TODO Il faut regénérer le stroke
		 */
		stroke=  StrokeFactory.getStroke(edgeType, edgeWidth);//recup�rer le stroke fait
		
	}

	/**
	 * Mise en place d'un nouvel état de ligne pointillée
	 * @param type le nouveau type de ligne
	 */
	public void setEdgeType(LineType type)
	{
		edgeType = type;
		/*
		 * TODO Il faut regénérer le stroke
		 */
		stroke=  StrokeFactory.getStroke(edgeType, edgeWidth);//reg�n�rer le stroke
		

	}

	/**
	 * Initialisation d'une figure de type {@link #type} au point p et ajout de
	 * cette figure à la liste des {@link #figures}
	 * @param p le point où initialiser la figure
	 * @return la nouvelle figure créée à x et y avec les paramètres courants
	 */
	public Figure initiateFigure(Point2D p)
	{
		/*
		 * TODO Maintenant que l'on s'apprête effectivement à créer une figure on
		 * ajoute/obtient les Paints et le Stroke des factories
		 */

		/*
		 * TODO Obtention de la figure correspondant au type de figure choisi grâce à
		 * type.getFigure(....)
		 */
		Figure newFigure = type.getFigure(stroke, edgePaint, fillPaint, p);
		figures.add(newFigure);
		notifyObservers();
		return newFigure; //TODO remplacer par type.getFigure(...)

		/*
		 * TODO Ajout de la figure à #figures
		 */
		/* TODO Notification des observers */
	}

	/**
	 * Obtention de la dernière figure (implicitement celle qui est en cours de
	 * dessin)
	 * @return la dernière figure du dessin
	 */
	public Figure getLastFigure()
	{
		// TODO Remplacer par l'implémentation ...
		return figures.lastElement();//l'impl�mentation
	}

	/**
	 * Obtention de la dernière figure contenant le point p.
	 * @param p le point sous lequel on cherche une figure
	 * @return une référence vers la dernière figure contenant le point p ou à
	 * défaut null.
	 */
	public Figure getFigureAt(Point2D p)
	{
		selectedFigure = null;

		/*
		 * TODO Recherche dans le flux des figures de la DERNIERE figure
		 * contenant le point p.
		 * 
		 */
		
		Iterator<Figure> it=figures.iterator();
		while(it.hasNext()){
		Figure  currentFigure = it.next();
			if(currentFigure.contains(p)){
				
				selectedFigure=currentFigure;
			
			}
		}
		return selectedFigure;
	}

	/**
	 * Retrait de la dernière figure
	 * @post le modèle de dessin a été mis à jour
	 */
	public void removeLastFigure()
	{
		// TODO Compléter ...
		if(!figures.isEmpty()){
			figures.removeElement(figures.lastElement());
			update();
			}
		//Complet
	}

	/**
	 * Effacement de toutes les figures (sera déclenché par une action clear)
	 * @post le modèle de dessin a été mis à jour
	 */
	public void clear()
	{
		// TODO Compléter ...
		figures.removeAllElements();
		update();
		//Complet
	}

	/**
	 * Accesseur de l'état de filtrage
	 * @return l'état courant de filtrage
	 */
	public boolean getFiltering()
	{
		return filtering ;
	}

	/**
	 * Changement d'état du filtrage
	 * @param filtering le nouveau statut de filtrage
	 * @post le modèle de dessin a été mis à jour
	 */
	public void setFiltering(boolean filtering)
	{
		// TODO ... filtering ...
		this.filtering = filtering;
		update();
		
	}

	/**
	 * Ajout d'un filtre pour filtrer les types de figures
	 * @param filter le filtre à ajouter
	 * @return true si le filtre n'était pas déjà présent dans l'ensemble des
	 * filtres fitrant les types de figures, false sinon
	 * @post si le filtre a été ajouté, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean addShapeFilter(ShapeFilter filter)
	{
		// TODO ... shapeFilters ...
		boolean added = false;
		if (!shapeFilters.contains(filter)) {
			shapeFilters.add(filter);
			added = true;
			update();
		}
		return added;
	}
	//complet
	/**
	 * Retrait d'un filtre filtrant les types de figures
	 * @param filter le filtre à retirer
	 * @return true si le filtre faisait partie des filtres filtrant les types
	 * de figure et a été retiré, false sinon.
	 * @post si le filtre a éré retiré, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean removeShapeFilter(ShapeFilter filter)
	{
		// TODO shapeFilters ....
		boolean removed = false;
		if (shapeFilters.contains(filter)) {
			shapeFilters.remove(filter);
			removed = true;
			update();
		}

		return removed;
		//complet...
	}
	

	/**
	 * Mise en place du filtre de couleur de remplissage
	 * @param filter le filtre de couleur de remplissage à appliquer
	 * @post le {@link #fillColorFilter} est mis en place et une mise à jour
	 * est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public void setFillColorFilter(FillColorFilter filter)
{
		// TODO ... fillColorFilter ...
		fillColorFilter = filter;
		update();
		//complet...
}

	/**
	 * Mise en place du filtre de couleur de trait
	 * @param filter le filtre de couleur de trait à appliquer
	 * @post le #edgeColorFilter est mis en place et une mise à jour
	 * est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public void setEdgeColorFilter(EdgeColorFilter filter)
	{
	// TODO ... edgeColorFilter ...
	edgeColorFilter = filter;
	update();
	//complet...
	}

	/**
	 * Ajout d'un filtre pour filtrer les types de ligne des figures
	 * @param filter le filtre à ajouter
	 * @return true si le filtre n'était pas déjà présent dans l'ensemble des
	 * filtres fitrant les types de lignes, false sinon
	 * @post si le filtre a été ajouté, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean addLineFilter(LineFilter filter)
	{
	// TODO lineFilters ...
	boolean ajouter = false;
	if (!lineFilters.contains(filter)) {
		lineFilters.add(filter);
		ajouter = true;
		
		update();

	}
	return ajouter;
	//complet...
	}

	/**
	 * Retrait d'un filtre filtrant les types de lignes
	 * @param filter le filtre à retirer
	 * @return true si le filtre faisait partie des filtres filtrant les types
	 * de lignes et a été retiré, false sinon.
	 * @post si le filtre a éré retiré, une mise à jour est déclenchée
	 */
	// TODO décommenter lorsque prêt
	public boolean removeLineFilter(LineFilter filter)
	{
//		// TODO lineFilters ...

	boolean remov = false;
	if (lineFilters.contains(filter)) {
		lineFilters.remove(filter);
		remov = true;
		update();
	}
	return remov;
	//complet...
	}

	/**
	 * Remise à l'état non sélectionné de toutes les figures
	 */
	public void clearSelection()
	{
		// TODO Compléter .....
		Figure current= null;
		Iterator<Figure> it=figures.iterator();
		while(it.hasNext()){
			current=(Figure) it.next();
			current.setSelected(false);
		//	System.out.println("bien delete");
		}
		//complet...
	}

	/**
	 * Mise à jour des indices des figures sélectionnées dans {@link #selectionIndex}
	 * d'après l'interrogation de l'ensembles des figures (après filtrage).
	 */
	public void updateSelection()
	{
		// TODO Compléter ...
		Figure current= null;
		Iterator<Figure> it=figures.iterator();
		int i=0;
		selectionIndex.clear();
		while(it.hasNext()){
			current=(Figure) it.next();
			if(current.isSelected()){
				selectionIndex.add(i);
			//	System.out.println("update");
			}
			i++;
		}
		update();
		
		//complet...
	}

	/**
	 * Indique s'il existe des figures sélectionnées
	 * @return true s'il y a des figures sélectionnées
	 */
	public boolean hasSelection()
	{
		// TODO Remplacer par l'implémentation
		return !selectionIndex.isEmpty();
		
		
	}

	/**
	 * Destruction des figures sélectionnées.
	 * Et incidemment nettoyage de {@link #selectionIndex}
	 */
	public void deleteSelected()
	{
		// TODO Compléter ...
		
		
		updateSelection();
		Iterator<Integer> it = selectionIndex.iterator();
		int i=0;
		while(it.hasNext()){
			
			figures.removeElementAt(it.next()-i);
			i++;
		}
		updateSelection();
		update();
		//complet...
	}

	/**
	 * Applique un style particulier aux figure sélectionnées
	 * @param fill la couleur de remplissage à applique aux figures sélectionnées
	 * @param edge la couleur de trait à appliquer aux figures sélectionnées
	 * @param stroke le style de trait à appliquer aux figures sélectionnées
	 */
	public void applyStyleToSelected(Paint fill, Paint edge, BasicStroke stroke)
	{
		// TODO Compléter ...
		
		
		Figure currentFigure= null;
		updateSelection();
		Iterator<Integer> it = selectionIndex.iterator();
		while(it.hasNext()){
			currentFigure=figures.get(it.next());
			currentFigure.setEdgePaint(edge);
			currentFigure.setFillPaint(fill);
			currentFigure.setStroke(stroke);
			
		}
		update();
		//complet...
	}

	/**
	 * Déplacement des figures sélectionnées en haut de la liste des figures.
	 * En conservant l'ordre des figures sélectionnées
	 */
	public void moveSelectedUp()
	{
		// TODO Compléter ...
				Figure currentFigure= null;
				Iterator<Integer> it = selectionIndex.iterator();
				int i=0;
				while(it.hasNext()){
					i=it.next();
					currentFigure=figures.get(i);
					figures.removeElementAt(i);
					figures.addElement(currentFigure);
				}
				notifyObservers();

				updateSelection();
				update();
				//complet...
	}
	
	//ajout� par moi meme 
	public void moveSelecteddown()
	{

		Figure currentFigure= null;
		Iterator<Integer> it = selectionIndex.iterator();
		int i=0;
		int j=0;
		while(it.hasNext()){
			i=it.next();
			currentFigure=figures.get(i);
			figures.removeElementAt(i);
			figures.add(j,currentFigure);
			j++;
		}
		notifyObservers();

		updateSelection();
		update();
	}

	/**
	 * Accès aux figures dans un stream afin que l'on puisse y appliquer
	 * de filtres
	 * @return le flux des figures éventuellement filtrés par les différents
	 * filtres
	 */
	public Stream<Figure> stream()
	{
		Stream<Figure> figuresStream = figures.stream();
		if (filtering)
		{
			// TODO Compléter avec ...
			// if (filters.size() > 0)
			// {
			// figuresStream = figuresStream.filter(filters);
			// }
			
			if (shapeFilters.size() > 0)
			{figuresStream = figuresStream.filter(shapeFilters);}

			if (fillColorFilter != null)
			{figuresStream = figuresStream.filter(fillColorFilter);}

			if (edgeColorFilter != null)
			{figuresStream = figuresStream.filter(edgeColorFilter);}

			if (lineFilters.size() > 0)
			{figuresStream = figuresStream.filter(lineFilters);}
		}
		return figuresStream;
		//complet...
	}

	/* (non-Javadoc)
	 * @see history.Originator#createMemento()
	 */
	@Override
	public Memento<Figure> createMemento()
	{
		return new Memento<Figure>(figures);
	}

	/* (non-Javadoc)
	 * @see history.Originator#setMemento(history.Memento)
	 */
	@Override
	public void setMemento(Memento<Figure> memento)
	{
		if (memento != null)
		{
			List<Figure> savedFigures = memento.getState();
			System.out.println("Drawing::setMemento(" + savedFigures + ")");

			figures.clear();
			for (Figure elt : savedFigures)
			{
				figures.add(elt.clone());
			}

			update();
		}
		else
		{
			System.err.println("Drawing::setMemento(null)");
		}
	}
}
