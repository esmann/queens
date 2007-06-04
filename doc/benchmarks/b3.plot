set terminal pdf
set out "b3.pdf"
set autoscale
unset log
unset label
set yrange [0:750]
set xrange [0:14]
set xtics (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14)
set ytic auto
set title "NQueens (Size 18)"
set xlabel ""
set ylabel "Time (seconds)"
plot 'bar-graph-data' using 1:3 title '' with boxes fs solid 0.5 lt 2
