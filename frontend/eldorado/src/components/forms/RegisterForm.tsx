import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {useToast} from "@/components/ui/use-toast.ts";
import {isValidPhoneNumber} from "react-phone-number-input";
import {PhoneInput} from "@/components/ui/phone-input.tsx";
import {useTranslation} from "react-i18next";

export function RegisterForm() {
    const {toast} = useToast()
    const {t} = useTranslation();

    const formSchema = z.object({
        email: z.string().min(1, {message: t("registerPage.emptyEmail")})
            .email(t("registerPage.wrongEmail")),
        login: z.string().min(3, {message: t("registerPage.loginTooShort")})
            .max(50, {message: t("registerPage.loginTooLong")}),
        password: z.string().min(8, {message: t("registerPage.passwordTooShort")})
            .max(60, {message: t("registerPage.passwordTooLong")}),
        firstName: z.string().min(2, {message: t("registerPage.firstNameTooShort")})
            .max(50, {message: t("registerPage.firstNameTooLong")}),
        lastName: z.string().min(2, {message: t("registerPage.lastNameTooShort")})
            .max(50, {message: t("registerPage.lastNameTooLong")}),
        phoneNumber: z.string().refine(isValidPhoneNumber, {message: t("registerPage.phoneNumberInvalid")}),
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            login: "",
            password: "",
            email: "",
            firstName: "",
            lastName: "",
            phoneNumber: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        // console.table(values);
        api.registerClient(
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
                    title: t("registerPage.popUp.registerOK.title"),
                    description: t("registerPage.popUp.registerOK.text"),
                });
            })
            .catch((error) => {
                if (error.response && error.response.data) {
                    const {message, violations} = error.response.data;
                    const violationMessages = violations.map((violation: string | string[]) => t(violation)).join(", ");

                    toast({
                        variant: "destructive",
                        title: t(message),
                        description: violationMessages,
                    });
                } else {
                    toast({
                        variant: "destructive",
                        description: "Error",
                    });
                }
                // console.log(error.response ? error.response.data : error);
            });
    }

    return (
        <Card className="mx-auto max-w-2xl">
            <CardHeader>
                <CardTitle className="text-2xl">{t("registerPage.title")}</CardTitle>
                <CardDescription>
                    {t("registerPage.info")}
                </CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                        <div className="grid gap-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="firstName"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel
                                                    className="text-black">{t("registerPage.firstName")}</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="lastName"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel
                                                    className="text-black">{t("registerPage.lastName")}</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="grid grid-cols-2 gap-4">
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="phoneNumber"
                                        render={({field}) => (
                                            <FormItem className="items-start">
                                                <FormLabel
                                                    className="text-black text-center">{t("registerPage.phoneNumber")}</FormLabel>
                                                <FormControl className="w-full">
                                                    {// @ts-expect-error - fix this maybe
                                                        <PhoneInput //TODO fix this
                                                            placeholder="" {...field}/>}
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <FormField
                                        control={form.control}
                                        name="email"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel className="text-black">{t("registerPage.email")}</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="mail@example.com" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="grid gap-2">
                                <FormField
                                    control={form.control}
                                    name="login"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel className="text-black">{t("registerPage.login")}</FormLabel>
                                            <FormControl>
                                                <Input placeholder="" {...field}/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="grid gap-2">
                                <FormField
                                    control={form.control}
                                    name="password"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel className="text-black">{t("registerPage.password")}</FormLabel>
                                            <FormControl>
                                                <Input type="password" {...field}/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <Button type="submit" className="w-full">
                                {t("registerPage.submit")}
                            </Button>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            {t("registerPage.hasAccount")}{" "}
                            <a href="/login" className="font-medium text-black hover:text-blue-500">
                                {t("registerPage.logIn")} </a>
                        </div>
                    </form>
                </Form>
            </CardContent>
        </Card>
    )
}

export default RegisterForm
