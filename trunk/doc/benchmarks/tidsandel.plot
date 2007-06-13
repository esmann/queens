set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "tidsandel.pdf"
set autoscale	
unset border
set xtics nomirror
set ytics nomirror
set title  'Andel af tid vs. Andel af l�sninger (N=18)'
set xrange[1:9]
set xlabel 'Placeringen af dronning i f�rste r�kke'
set ylabel '% af total for alle midterbr�t'
plot 'tidsandel.data' using 1:2 with linespoints title 'Andel i Tid', \
	  ''  using 1:3 with linespoints title 'L�sninger'
	
