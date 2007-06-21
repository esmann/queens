set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "unikkevstotale.pdf"
set autoscale
set title "Unikke vs Totale løsninger"
set xtics (15, 16, 17, 18)
set xlabel "N"
set ylabel "Antal løsninger"
set boxwidth 0.2 absolute
plot 'unikkevstotale-data' using 1:2 title 'unikke' with linespoints, \
		 ''               using 1:3 title 'totale' with linespoints
