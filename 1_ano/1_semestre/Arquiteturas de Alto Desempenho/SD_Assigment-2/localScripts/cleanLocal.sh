echo "Removing .zip files"
cd ..
rm -rf *.zip

echo "Removing generated folders"
rm -rf generalRepository
rm -rf assaultParty
rm -rf concentrationSite
rm -rf controlSite
rm -rf museum
rm -rf ordinaryThieves
rm -rf masterThief

echo "Removing generated .class files"
rm -rf */*.class
rm -rf */*/*.class