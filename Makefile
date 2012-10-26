name=voms-api-java
spec=spec/$(name).spec
version=$(shell grep "Version:" $(spec) | sed -e "s/Version://g" -e "s/[ \t]*//g")
pom=pom.xml
pom_version=$(shell grep "<version>" $(pom) | head -1 | sed -e 's/<version>//g' -e 's/<\/version>//g' -e "s/[ \t]*//g")
release=1
tarbuild_dir=$(shell pwd)/tarbuild
rpmbuild_dir=$(shell pwd)/rpmbuild
bc_version=1.45
#mvn_settings=-s src/config/emi-build-settings.xml

.PHONY: clean rpm prepare-sources prepare-spec

all: 	dist rpm

prepare-sources: prepare-spec
		rm -rf 	$(tarbuild_dir)
		mkdir -p $(tarbuild_dir)/$(name)
		cp -r AUTHORS LICENSE Makefile README.md pom.xml spec src $(tarbuild_dir)/$(name)
		cd $(tarbuild_dir) && tar cvzf $(tarbuild_dir)/$(name)-$(version).tar.gz $(name)
		
prepare-spec:
		sed -e 's#@@MVN_SETTINGS@@#$(settings_file)#g' \
			-e 's#@@BC_VERSION@@#$(bc_version)#g' \
			-e 's#@@POM_VERSION@@#$(pom_version)#g' \
			spec/voms-api-java.spec.in > spec/voms-api-java.spec
clean:	
		rm -rf target $(rpmbuild_dir) $(tarbuild_dir) tgz RPMS dir spec/voms-api-java.spec

dist:   prepare-sources

rpm:		
		mkdir -p 	$(rpmbuild_dir)/BUILD $(rpmbuild_dir)/RPMS \
					$(rpmbuild_dir)/SOURCES $(rpmbuild_dir)/SPECS \
					$(rpmbuild_dir)/SRPMS

		cp $(tarbuild_dir)/$(name)-$(version).tar.gz $(rpmbuild_dir)/SOURCES/$(name)-$(version).tar.gz
		rpmbuild --nodeps -v -ba $(spec) --define "_topdir $(rpmbuild_dir)" 
