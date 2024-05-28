import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {Controller, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {useToast} from "@/components/ui/use-toast.ts";
import {isValidPhoneNumber} from "react-phone-number-input";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {useTranslation} from "react-i18next";
import {Loader2, Slash} from "lucide-react";
import {useState} from "react";
import handleApiError from "@/components/HandleApiError.ts";
import SiteHeader from "@/components/SiteHeader.tsx";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Card, CardContent, CardHeader} from "@/components/ui/card.tsx";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group.tsx";
import {Label} from "@/components/ui/label.tsx";
import {useNavigate} from "react-router-dom";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";

export function AdminCreateUserPage() {
    const {toast} = useToast()
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [formValues, setFormValues] = useState(null);

    const formSchema = z.object({
        email: z.string().min(1, {message: t("registerPage.emptyEmail")})
            .email(t("registerPage.wrongEmail")),
        login: z.string().min(3, {message: t("registerPage.loginTooShort")})
            .max(50, {message: t("registerPage.loginTooLong")}),
        password: z.string()
            .min(8, {message: t("registerPage.passwordTooShort")})
            .max(60, {message: t("registerPage.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
        firstName: z.string().min(2, {message: t("registerPage.firstNameTooShort")})
            .max(50, {message: t("registerPage.firstNameTooLong")}),
        lastName: z.string().min(2, {message: t("registerPage.lastNameTooShort")})
            .max(50, {message: t("registerPage.lastNameTooLong")}),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("registerPage.phoneNumberInvalid")}),
        newPasswordRepeat: z.string().min(8, {message: t("accountSettings.passwordTooShort")})
            .max(50, {message: t("accountSettings.passwordTooLong")})
            .regex(/(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,32}$/, {message: t("registerPage.passwordInvalid")}),
        role: z.enum(["client", "admin", "staff"]).default("client")
    }).refine(values => values.password === values.newPasswordRepeat, {
        message: t("accountSettings.passwordsMustMatch"),
        path: ["newPasswordRepeat"],
    });

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            login: "",
            password: "",
            email: "",
            firstName: "",
            lastName: "",
            phoneNumber: "",
            role: "client"
        },
    });

    const onSubmitUser = (values: z.infer<typeof formSchema>) => {
        // @ts-expect-error - fix this
        setFormValues(values);
        setAlertDialogOpen(true);
    };

    const handleDialogAction = () => {
        // @ts-expect-error - fix this
        submitUser(formValues)
        setAlertDialogOpen(false);
    };

    function submitUser(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        const registerFn = {
            client: api.registerClient,
            admin: api.registerAdmin,
            staff: api.registerStaff,
        }[values.role];

        registerFn(
            values.login,
            values.password,
            values.firstName,
            values.lastName,
            values.email,
            values.phoneNumber,
            navigator.language.substring(0, 2)
        )
            .then(() => {
                toast({
                    title: t("registerPage.admin.popUp.registerOK.title"),
                    description: t("registerPage.admin.popUp.registerOK.text"),
                    action: (
                        <div>
                            <Button onClick={() => {
                                navigate('/manage-users', {replace: true});
                            }}>
                                {t("registerPage.admin.popUp.registerOK.button")}
                            </Button>
                        </div>
                    ),
                });
            })
            .catch((error) => {
                handleApiError(error);
            }).finally(() => {
            setIsLoading(false);
        });
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/manage-users">{t("breadcrumb.manageUsers")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.createUser")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">Create a new User</h1>
                </div>
                <Card className="mx-auto max-w-2xl">
                    <CardHeader>
                    </CardHeader>
                    <CardContent>
                        <Form {...form}>
                            <form onSubmit={form.handleSubmit(onSubmitUser)} className="space-y-4">
                                <div className="grid gap-4">
                                    <div className="grid grid-cols-2 gap-4">
                                        <div className="grid gap-2">
                                            <FormField
                                                control={form.control}
                                                name="firstName"
                                                render={({field}) => (
                                                    <FormItem>
                                                        <FormLabel
                                                            className="text-black">{t("registerPage.firstName")} *</FormLabel>
                                                        <FormControl>
                                                            <Input placeholder="" {...field} />
                                                        </FormControl>
                                                        <FormMessage/>
                                                    </FormItem>
                                                )}/>
                                        </div>
                                        <div className="grid gap-2">
                                            <FormField
                                                control={form.control}
                                                name="lastName"
                                                render={({field}) => (
                                                    <FormItem>
                                                        <FormLabel
                                                            className="text-black">{t("registerPage.lastName")} *</FormLabel>
                                                        <FormControl>
                                                            <Input placeholder="" {...field} />
                                                        </FormControl>
                                                        <FormMessage/>
                                                    </FormItem>
                                                )}/>
                                        </div>
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div className="grid gap-2">
                                            <FormField
                                                control={form.control}
                                                name="phoneNumber"
                                                render={() => (
                                                    <FormItem className="items-start">
                                                        <FormLabel
                                                            className="text-black text-center">{t("registerPage.phoneNumber")} *</FormLabel>
                                                        <FormControl className="w-full">
                                                            <Controller
                                                                name="phoneNumber"
                                                                control={form.control}
                                                                render={({field}) => (
                                                                    //ts-expect-error - PhoneInput is not typed
                                                                    <PhoneInput
                                                                        {...field}
                                                                        value={field.value as never || ""}
                                                                        onChange={field.onChange}
                                                                        countries={['PL']}
                                                                        defaultCountry="PL"
                                                                    >
                                                                    </PhoneInput>
                                                                )}/>
                                                        </FormControl>
                                                        <FormMessage/>
                                                    </FormItem>
                                                )}/>
                                        </div>
                                        <div className="grid gap-2">
                                            <FormField
                                                control={form.control}
                                                name="email"
                                                render={({field}) => (
                                                    <FormItem>
                                                        <FormLabel
                                                            className="text-black">{t("registerPage.email")} *</FormLabel>
                                                        <FormControl>
                                                            <Input placeholder="mail@example.com" {...field} />
                                                        </FormControl>
                                                        <FormMessage/>
                                                    </FormItem>
                                                )}/>
                                        </div>
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={form.control}
                                            name="login"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("registerPage.login")} *</FormLabel>
                                                    <FormControl>
                                                        <Input placeholder="" {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}/>
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={form.control}
                                            name="password"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("registerPage.password")} *</FormLabel>
                                                    <FormControl>
                                                        <Input type="password" {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}/>
                                        <FormField
                                            control={form.control}
                                            name="newPasswordRepeat"
                                            render={({field}) => (
                                                <FormItem>
                                                    <FormLabel
                                                        className="text-black">{t("registerPage.passwordRepeat")} *</FormLabel>
                                                    <FormControl>
                                                        <Input type="password" {...field} />
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}/>
                                    </div>
                                    <div className="grid gap-2">
                                        <FormField
                                            control={form.control}
                                            name="role"
                                            render={({field}) => (
                                                <FormItem className={"space-y-3"}>
                                                    <FormLabel
                                                        className="text-black">{t("registerPage.role")}</FormLabel>
                                                    <FormControl>
                                                        <RadioGroup
                                                            value={field.value}
                                                            onValueChange={field.onChange}
                                                            className="flex flex-col space-y-3"
                                                        >
                                                            <FormItem className=" items-center space-x-2">
                                                                <FormControl>
                                                                    <RadioGroupItem value="client">
                                                                        <Label>{t("registerPage.client")}</Label>
                                                                    </RadioGroupItem>
                                                                </FormControl>
                                                                <FormLabel>
                                                                    {t("registerPage.client")}
                                                                </FormLabel>
                                                            </FormItem>
                                                            <FormItem className=" items-center space-x-2">
                                                                <FormControl>
                                                                    <RadioGroupItem value="staff">
                                                                        <Label>{t("registerPage.staff")}</Label>
                                                                    </RadioGroupItem>
                                                                </FormControl>
                                                                <FormLabel>
                                                                    {t("registerPage.staff")}
                                                                </FormLabel>
                                                            </FormItem>
                                                            <FormItem className=" items-center space-x-2">
                                                                <FormControl>
                                                                    <RadioGroupItem value="admin">
                                                                        <Label>{t("registerPage.admin")}</Label>
                                                                    </RadioGroupItem>
                                                                </FormControl>
                                                                <FormLabel>
                                                                    {t("registerPage.admin")}
                                                                </FormLabel>
                                                            </FormItem>
                                                        </RadioGroup>
                                                    </FormControl>
                                                    <FormMessage/>
                                                </FormItem>
                                            )}/>
                                    </div>
                                    <Button type="submit" className="w-full" disabled={isLoading}>
                                        {isLoading ? (
                                            <>
                                                <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                            </>
                                        ) : (
                                            t("registerPage.submit")
                                        )}
                                    </Button>
                                </div>
                            </form>
                        </Form>
                    </CardContent>
                </Card>
                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                    <AlertDialogContent>
                        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                        <AlertDialogDescription>
                            {t("adminAddUserPage.confirmUserCreation")}
                        </AlertDialogDescription>
                        <AlertDialogAction onClick={handleDialogAction}>
                            {t("general.ok")}
                        </AlertDialogAction>
                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                    </AlertDialogContent>
                </AlertDialog>
            </main>
        </div>
    )
}

export default AdminCreateUserPage
