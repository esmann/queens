set terminal pdf fname "Helvetica" fsize "4" 
set out "middleandel.pdf"
set autoscale	
unset border
set xtics nomirror
set ytics nomirror
set title  'Andel af l�sninger for forskellige midterbr�t'
set xrange[1:9]
set xlabel 'Placeringen af dronning i f�rste r�kke'
set ylabel 'Andel af l�sninger i %'
plot 'middleandel.data' index 0 with linespoints title 'N=12', \
	'middleandel.data' index 1 with linespoints title  'N=13' , \
	'middleandel.data' index 2 with linespoints  title 'N=14', \
	'middleandel.data' index 3 with linespoints title 'N=15', \
	'middleandel.data' index 4 with linespoints title 'N=16', \
	'middleandel.data' index 5 with linespoints title 'N=17', \
	'middleandel.data' index 5 with linespoints title 'N=18'
	
	
