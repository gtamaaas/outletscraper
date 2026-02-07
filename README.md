# Outlet Scraper 🛒

A Java Spring Boot web scraper built with Selenium that collects product data from a popular outlet website and stores it in MongoDB.

# 📁 Project Structure
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── OutletScraper
│   │               ├── controller        
│   │               ├── dto
│   │               │   └── scrapeResult  
│   │               ├── fileReaders       
│   │               ├── model
│   │               │   └── alert        
│   │               ├── repository        
│   │               ├── scraper           
│   │               └── service            
│   └── resources
│       ├── data                                             
│       └── templates                      
│
└── test
    ├── java
    │   └── com
    │       └── example
    │           └── OutletScraper
    │               ├── config             
    │               ├── integrationTests   
    │               └── scraperTests       
    └── resources
        └── testSites                      

```
# 🚀 Features
## 🔍 Scraping

- Scrapes product data from  using Selenium

- Persists results directly into a MongoDB database

## 📄 File Input

- Supports scraping multiple urls/products from a .txt file

- Ideal for batch scraping and automation

## 🚨 Alerts

- Price drop / price increase notifications

- Back-in-stock alerts

- Alerts when an item becomes unavailable

## 📊 Analytics

- Track historical pricing

- Compute analytics such as:

  - Lowest recorded price

  - Price changes over time

## 🌐 REST API

- Exposes scraped and processed data via REST endpoints

- Designed for easy integration with a future frontend (web or mobile)

## 🧪 Testing

- Unit and integration tests

- Uses dummy HTML files to mimic real outlet site behavior


