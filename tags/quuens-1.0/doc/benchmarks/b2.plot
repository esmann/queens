set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "b2.pdf"
set autoscale
unset log
unset label
set yrange [0:750]
set xtic auto
set ytic auto
set title "NQueens (Size 18)"
set xlabel ""
set ylabel "Time (seconds)"
plot 'benchmark_java_18_0_true_2007-06-03-15-01-22' using 4 title 'Java rekursiv (0)' with linespoints, \
     'benchmark_java_18_1_true_2007-06-03-00-13-44' using 4 title 'Java rekursiv (1)' with linespoints, \
     'benchmark_java_18_2_true_2007-06-03-12-31-14' using 4 title 'Java rekursiv (2)' with linespoints, \
     'benchmark_java_18_3_true_2007-06-03-13-04-43' using 4 title 'Java rekursiv (3)' with linespoints
