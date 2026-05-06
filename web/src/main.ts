import "./style.css";
import { mountGeoMania } from "./game";

const root = document.getElementById("app");

if (!root) {
  throw new Error("Could not find #app");
}

mountGeoMania(root);
