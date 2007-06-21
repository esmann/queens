set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "b3.pdf"
set autoscale
unset log
unset label
set yrange [0:750]
set xrange [0:19]
set ytics auto nomirror
set xtics (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18) nomirror
unset xtics
set title "NQueens (Size 18)"
set xlabel ""
set ylabel "Time (seconds)"
unset yzeroaxis 
unset x2zeroaxis
unset xzeroaxis
unset border
set bmargin 10
set grid noxtics ytics front linetype rgb "#FFFFFF"
plot 'bar-graph-data' using 1:3 title '' with boxes fs solid 0.5 lt 2, \
	   ''								using 1:3:3 with labels center offset 0,1 notitle, \
		 ''							  using 1:4:2 with labels right rotate by 50 offset graph -0.001,-0.03 notitle
