#pragma once

#include <iostream>
#include <vector>
#include <string>
#include "data/BlockLoader.hh"
#include <cstring>


//constexpr float NaN = 0xffffffff;

class GapMinderLoader : public BlockLoader{
public:

struct Header{
  uint32_t numCountries;
  uint32_t numDatasets;
  uint32_t numDataPoints;
};

  #include "Dataset.hh"

  const Header *header;
  const char *countryCodes;
  const uint8_t *continents;
  const Dataset* indices;
  const float* data;

public:
  GapMinderLoader(const char filename[]);
  
  const Dataset* getDataset(const char dataset[]) const;
  float getData(uint32_t countryIndex, uint32_t year, const Dataset* d) const;
  std::vector<float> getAllDataOneYear(uint32_t year, const Dataset* d) const;
  std::vector<float> getAllDataOneCountry(uint32_t countryIndex, const Dataset* d);
};


