export function shouldShowNavbar(currentPath, routes, userRole) {
  console.log("Current path:", currentPath);
  console.log("Routes:", routes);
  console.log("User role:", userRole);

  if (!userRole) userRole = "ROLE_USER";

  const pathToRegex = (path) =>
    new RegExp("^" + path.replace(/:\w+/g, "[^/]+") + "$");

  return routes.some(
    (route) =>
      (route.role === userRole || userRole === "ROLE_ADMIN") &&
      pathToRegex(route.path).test(currentPath)
  );
}
