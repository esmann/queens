#!/usr/bin/python 
# 
# This MiG python script was autogenerated by the MiG User Script Generator !!!
# Any changes should be made in the generator and not here !!!

import sys
import os
import getopt

def version():
	print "MiG User Scripts: $Revision: 1.32 $"

# end version

def usage():

        print "Usage: migrm.py [OPTIONS] FILE [FILE ...]"
        print "Where OPTIONS include:"
        print "-r		recursive"
        print "-v		verbose mode"
        print "-V		display version"
        print "-h		display this help"
        print "-c CONF		read configuration from CONF instead of"
        print "		default (~/.mig/miguser.conf)."
        print "-s MIG_SERVER	force use of MIG_SERVER."
# end usage

def check_var(name, var):

        if not var:
           print name + " not set! Please set in configuration file or through the command line"
           sys.exit(1)
# end version

def read_conf(conf, option):

        try:
            conf_file = open(conf, 'r')
            for line in conf_file.readlines():
                line = line.strip()
                # split on any whitespace and assure at least two parts
                parts = line.split() + ['', '']
                opt, val = parts[0], parts[1]
                if opt == option:
                    return val
            conf_file.close()
        except Exception, e:
            return ''

# end usage

def rm_file(path_list):

        if not ca_cert_file:
           ca_check = '--insecure'
        else:
           ca_check = "--cacert %s" % (ca_cert_file)

        if not password:
           password_check = ''
        else:
           password_check = "--pass %s" % (password)

        timeout = ''
        if max_time:
           timeout += "--max-time %s" % (max_time)
        if connect_timeout:
           timeout += " --connect-timeout %s" % (connect_timeout)

        curl = "curl "
        status = os.system("%s --fail --cert %s --key %s %s %s %s --url '%s/cgi-bin/rm.py?%s;flags=%s;with_html=false'" % (curl, cert_file, key_file, ca_check, password_check, timeout, mig_server, path_list, server_flags)) 
        return status >> 8

# end rm_file


# === Main ===

recursive = 0
verbose = 0
conf = os.path.expanduser("~/.mig/miguser.conf")
flags = ""
mig_server = ""
server_flags = ""
opt_args = "c:hrs:vV"

# preserve arg 0
arg_zero = sys.argv[0]
args = sys.argv[1:]
try:
        opts, args = getopt.getopt(args, opt_args)
except getopt.GetoptError, e:
        print "Error: ", e.msg
        usage()
        sys.exit(1)

for (opt, val) in opts:
        if opt == "-r":
                recursive = True
                server_flags += "r"
        elif opt == "-s":
                mig_server = val
        elif opt == "-v":
                verbose = True
        elif opt == "-V":
                version()
                sys.exit(0)
        elif opt == "-h":
                usage()
                sys.exit(0)
        elif opt == "-c":
                conf = val

        else:
                print "Error: %s not supported!" % (opt)

        # Drop options while preserving original sys.argv[0] 
        sys.argv = [arg_zero] + args
arg_count = len(sys.argv) - 1
min_count = 1

if arg_count < min_count:
   print "Too few arguments: got %d, expected %d!" % (arg_count, min_count)
   usage()
   sys.exit(1)

if not os.path.isfile(conf):
   print "Failed to read configuration file: %s" % (conf)
   sys.exit(1)

if verbose:
    print "using configuration in %s" % (conf)

if not mig_server:
   mig_server = read_conf(conf, 'migserver')

cert_file = read_conf(conf, 'certfile')
key_file = read_conf(conf, 'keyfile')
ca_cert_file = read_conf(conf, 'cacertfile')
password = read_conf(conf, 'password')
connect_timeout = read_conf(conf, 'connect_timeout')
max_time = read_conf(conf, 'max_time')

check_var("migserver", mig_server)
check_var("certfile", cert_file)
check_var("keyfile", key_file)

# Build the path_list string used in wild card expansion:
# 'path=$1;path=$2;...;path=$N'
path_list = "path=%s" % ";path=".join(sys.argv[1:])
rm_file(path_list)
