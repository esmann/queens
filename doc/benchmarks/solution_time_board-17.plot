set terminal pdf fname "Helvetica" fsize "4"
set out "solution-time-board-17.pdf"
set autoscale
set ytics auto nomirror
set y2tics auto
set title "NQueens (Size 17)"
set xlabel " "
set y2label "Time (milliseconds)"
set ylabel "antal løsninger"
unset xtics
unset border
plot 'solution-time-board-17-data' using 1:4 title 'totale' with linespoints, \
		 '' using 1:5 with linespoints title 'unikke', \
     '' using 1:3 with linespoints title 'tid (rek)' axes x1y2, \
     '' using 1:7 axes x1y2 title 'tid (ite)' with linespoints, \
		 '' using 1:6:2 with labels offset 0,-1
