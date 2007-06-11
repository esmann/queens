set terminal pdf fname "Helvetica" fsize "4"
set out "resultatstorrelse.pdf"
set autoscale
unset title 
set ylabel "Q(n)/Q(n-1)"
set xlabel "n"
set ytics nomirror
set xtics nomirror
set boxwidth 0.2 absolute
unset label
unset border
plot 'resultatstørrelse.data' title '' with linespoints 



