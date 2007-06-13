set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "maxantal.pdf"
set xtic auto
set ytic auto
set autoscale
set title "Max antal løsninger"
set xlabel "N"
set ylabel "Log(Q(N))"
set logscale y
plot 'maxantal.data' using 1:3 title 'Løsninger' with linespoints, \
     '' using 1:4 title '2^63-1' with lines, \
     '' using 1:2 title '2^31-1' with lines

