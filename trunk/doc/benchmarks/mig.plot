set terminal pdf fname "Helvetica" fsize "4"
set out "mig.pdf"
set autoscale
unset log
unset label
set xrange [0:9]
set ytics auto nomirror
unset xtics
set y2range [0:1]
set y2tics auto nomirror
set title "NQueens (Size 18)"
set xlabel ""
set ylabel "Time (seconds)"
set y2label "Speedup i forhold til java-0-true"
unset yzeroaxis 
unset xzeroaxis
unset border
set bmargin 10
set grid noxtics ytics front linetype rgb "#FFFFFF"
set boxwidth 0.2 absolute
plot 'bar-graph-data-mig' using 1:3 title '' with boxes fs solid 0.5 lt 2, \
	   ''								using 1:3:3 with labels center offset 0,1 notitle, \
	   ''               using 1:5 title '' with linespoints axes x1y2, \
		 ''							  using 1:4:2 with labels right rotate by 50 offset graph -0.001,-0.03 notitle 
