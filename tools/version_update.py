from subprocess import call
import sys
from xml.dom.minidom import parse

pom = open("pom.xml")
dom = parse(pom)
pom.close()
mv = dom.getElementsByTagName('motech.version')
mv[0].childNodes[0].data = sys.argv[1]

f = open("pom.xml", 'w')
dom.writexml(f)
f.close()
call(["git", "commit", "-am", "Update version"])
